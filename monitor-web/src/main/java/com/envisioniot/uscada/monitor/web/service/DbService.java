package com.envisioniot.uscada.monitor.web.service;

import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.web.component.HttpComponent;
import com.envisioniot.uscada.monitor.web.dao.rdb.DbDao;
import com.envisioniot.uscada.monitor.web.dao.tsdb.DbInfluxDao;
import com.envisioniot.uscada.monitor.web.entity.*;
import com.envisioniot.uscada.monitor.web.exception.WebRequestException;
import com.envisioniot.uscada.monitor.web.util.RDSConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;
import static com.envisioniot.uscada.monitor.common.util.CommConstants.POST_DB_CONN_TEST_URL;

/**
 *
 * @ClassName:DbInfoService.java
 * @version v2.3
 * @author: http://www.wgstart.com
 * @date: 2019年11月16日
 * @Description: DbService.java
 * @Copyright: 2019-2020 wgcloud. All rights reserved.
 *
 */
@Service
@Slf4j
public class DbService {

	@Autowired
	private DbDao dbDao;

	@Resource
	private DbInfluxDao dbInfluxDao;

	@Autowired
	private HttpComponent httpComponent;

	@Value("${host.matchFlag}")
	private String matchFlag;
	public List<DbInfo> qryDbList(String hostIp) {

		List<DbInfo> dbList =  dbDao.qryDbList(hostIp);
		if(!CollectionUtils.isEmpty(dbList)) {
			dbList.forEach(dbInfo -> dbInfo.setHostMatch(matchFlag.equals(dbInfo.getMatchFlag())));
		}
		return dbList;
	}

	public int updateById(DbInfo DbInfo) {
		return dbDao.updateById(DbInfo);
	}

	public void save(DbInfo dbInfo) {
		dbInfo.setId(UUID.randomUUID().toString());
		dbDao.save(dbInfo);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteByIds(List<String> ids) {
		List<DbSampleReq> dbSampleReqList = dbDao.qryDbSampleReqByIds(ids);
		List<TableSampleReq> tableSampleReqList = dbDao.qryTableSampleReqByDbIds(ids);
		if (!CollectionUtils.isEmpty(dbSampleReqList)) {
			for (DbSampleReq dbSampleReq : dbSampleReqList) {
				dbInfluxDao.delDbHis(dbSampleReq);
			}
			dbDao.deleteByIds(ids);
		}
		if (!CollectionUtils.isEmpty(tableSampleReqList)) {
			for (TableSampleReq tableSampleReq : tableSampleReqList) {
				dbInfluxDao.delTableHis(tableSampleReq);
			}
			delTableByDbIds(ids);
		}
	}

	public List<DbSampleResp> getDbSample(DbSampleReq dbSampleReq) {
		return dbInfluxDao.getDbSample(dbSampleReq);
	}

	public List<TableInfo> qryAllTable(){
		List<TableInfo> tableInfos = dbDao.qryAllTable();
		if(!CollectionUtils.isEmpty(tableInfos)) {
			tableInfos.forEach(tableInfo -> tableInfo.getDbInfo().setHostMatch(matchFlag.equals(tableInfo.getDbInfo().getMatchFlag())));
		}
		return tableInfos;
	}

	public void saveTableInfo(SaveTableReq saveTableReq) {
		dbDao.saveTableInfo(saveTableReq);
	}

	public int updTableById(SaveTableReq saveTableReq) {
		return dbDao.updTableById(saveTableReq);
	}

	@Transactional(rollbackFor = Exception.class)
	public void delTableByIds(List<Long> ids) {
		List<TableSampleReq> tableSampleReqList = dbDao.qryTableSampleReqByIds(ids);
		if (!CollectionUtils.isEmpty(tableSampleReqList)) {
			for (TableSampleReq tableSampleReq : tableSampleReqList) {
				dbInfluxDao.delTableHis(tableSampleReq);
			}
			dbDao.delTableByIds(ids);
		}
	}

//	@Transactional(rollbackFor = Exception.class)
	private void delTableByDbIds(List<String> dbIds) {
		dbDao.delTableByDbIds(dbIds);
	}

	public List<TableSampleResp> getTableSample(TableSampleReq tableSampleReq) {
		return dbInfluxDao.getTableSample(tableSampleReq);
	}

	public JdbcTemplate getJdbcTemplate(DbInfo dbInfo){
		JdbcTemplate jdbcTemplate = null;
		String driver = "";
		String url = "";
		if("mysql".equals(dbInfo.getDbType())){
			driver = RDSConnection.driver_mysql;
			url = RDSConnection.url_mysql;
		}else if("postgresql".equals(dbInfo.getDbType())){
			driver = RDSConnection.driver_postgresql;
			url = RDSConnection.url_postgresql;
		}else if("sqlserver".equals(dbInfo.getDbType())){
			driver = RDSConnection.driver_sqlserver;
			url = RDSConnection.url_sqlserver;
		}else if("db2".equals(dbInfo.getDbType())){
			driver = RDSConnection.driver_db2;
			url = RDSConnection.url_db2;
		}else if("oracle".equals(dbInfo.getDbType())){
			driver = RDSConnection.driver_oracle;
			url = RDSConnection.url_oracle;
		}
		url = url.replace("{ip}",dbInfo.getDbIp()).replace("{port}",dbInfo.getDbPort()).replace("{dbname}",dbInfo.getDbName());
		//创建连接池
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(dbInfo.getUser());
		dataSource.setPassword(dbInfo.getPasswd());
		jdbcTemplate = new JdbcTemplate(dataSource);
		if("mysql".equals(dbInfo.getDbType())){
			jdbcTemplate.queryForRowSet(RDSConnection.MYSQL_VERSION);
		}else if("postgresql".equals(dbInfo.getDbType())){
			jdbcTemplate.queryForRowSet(RDSConnection.MYSQL_VERSION);
		}else if("sqlserver".equals(dbInfo.getDbType())){
			jdbcTemplate.queryForRowSet(RDSConnection.SQLSERVER_VERSION);
		}else if("db2".equals(dbInfo.getDbType())){
			jdbcTemplate.queryForRowSet(RDSConnection.DB2_VERSION);
		}else if("oracle".equals(dbInfo.getDbType())){
			jdbcTemplate.queryForRowSet(RDSConnection.ORACLE_VERSION);
		}
		return jdbcTemplate;
	}

	public Response testDbConn(DbConnTestReq request) {
		String url = HTTP + request.getHostIp() + ":" + request.getPort() + "/" + POST_DB_CONN_TEST_URL;
		try {
			return httpComponent.postForServer(url, JSONUtil.parseObj((com.envisioniot.uscada.monitor.common.entity.DbInfo) request), 1);
		} catch (Exception e) {
			log.error("test db conn fail, url={}, request={}", url, request);
			log.error(e.getMessage(), e);
			throw new WebRequestException(e.getMessage());
		}
	}

	public List<AlarmResponse> getDbAlarm(String st, String et, String dbId) {
		try {
			return dbDao.getDbAlarm(st, et, dbId);
		} catch (Exception e) {
			log.error("get dbId={}, st={}, et={} db alarm fail.", dbId, st, et);
			log.error(e.getMessage(), e);
			throw new WebRequestException(e.getMessage());
		}
	}
}
