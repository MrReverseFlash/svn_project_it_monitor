package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.DbStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DbInfoCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.DbInfoMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbInfoMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/12 21:27
 */
public class DbInfoMysqlDao extends DbInfoCommDao {

    @Autowired
    private DbInfoMysqlMapper dbInfoMysqlMapper;

    @Override
    public void batchForUpdate(String hostIp, List<DbStat> sampleData) {
        dbInfoMysqlMapper.batchForUpdate(hostIp, sampleData);
    }

    @Override
    public void insertBatch(String hostIp, List<DbStat> dbInfo) {
        dbInfoMysqlMapper.insertBatch(hostIp, dbInfo);
    }

    @Override
    public void insert(String hostIp, DbStat db) {
        dbInfoMysqlMapper.insert(hostIp, db);
    }

    @Override
    public String selectIdByInfo(String hostIp, DbStat db) {
     return    dbInfoMysqlMapper.selectIdByInfo(hostIp, db);
    }

    @Override
    public String qryDbAlarmObjById(String dbId) {
        return dbInfoMysqlMapper.qryDbAlarmObjById(dbId);
    }
}
