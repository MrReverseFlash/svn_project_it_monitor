package com.envisioniot.uscada.monitor.web.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.web.config.InfluxdbConfig;
import com.envisioniot.uscada.monitor.web.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * DbInfluxDao
 *
 * @author yangkang
 * @date 2021/1/20
 */
@Repository
@Slf4j
public class DbInfluxDao {

    @Autowired
    private InfluxDB influxDB;

    public List<DbSampleResp> getDbSample(DbSampleReq dbSampleReq) {
        String influxSql = " select time, commit_num, conn_max, use_conn, questions, rollback_num, status" +
                           " from monitor_db_his" +
                           " where time >= " + DateUtil.parse(dbSampleReq.getSt()).getTime() + "ms" +
                           " and time <= " + DateUtil.parse(dbSampleReq.getEt()).getTime() + "ms" +
                           " and host_ip = '" + dbSampleReq.getHostIp() + "'" +
                           " and db_ip = '" + dbSampleReq.getDbIp() + "'" +
                           " and db_type = '" + dbSampleReq.getDbType() + "'" +
                           " and db_name = '" + dbSampleReq.getDbName() + "'";
        Query query = new Query(influxSql);
        QueryResult result = influxDB.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        List<DbSampleResp> samples = new LinkedList<>();
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return samples;
        }
        List<List<Object>> values = results.get(0).getSeries().get(0).getValues();
        if (CollectionUtil.isEmpty(results)) {
            return samples;
        }
        for (List<Object> item : values) {
            samples.add(new DbSampleResp(item));
        }
        return samples;
    }

    public List<TableSampleResp> getTableSample(TableSampleReq tableSampleReq) {
        String influxSql = " select time, record_num" +
                           " from monitor_table_his" +
                           " where time >= " + DateUtil.parse(tableSampleReq.getSt()).getTime() + "ms" +
                           " and time <= " + DateUtil.parse(tableSampleReq.getEt()).getTime() + "ms" +
                           " and host_ip = '" + tableSampleReq.getHostIp() + "'" +
                           " and db_ip = '" + tableSampleReq.getDbIp() + "'" +
                           " and db_type = '" + tableSampleReq.getDbType() + "'" +
                           " and db_name = '" + tableSampleReq.getDbName() + "'" +
                           " and table_name = '" + tableSampleReq.getTableName() + "'";
        Query query = new Query(influxSql);
        QueryResult result = influxDB.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        List<TableSampleResp> samples = new LinkedList<>();
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return samples;
        }
        List<List<Object>> values = results.get(0).getSeries().get(0).getValues();
        if (CollectionUtil.isEmpty(results)) {
            return samples;
        }
        for (List<Object> item : values) {
            samples.add(new TableSampleResp(item));
        }
        return samples;
    }

    public void delDbHis(DbSampleReq dbSampleReq) {
        String influxSql = " delete from monitor_db_his" +
                           " where host_ip = '" + dbSampleReq.getHostIp() + "'" +
                           " and db_ip = '" + dbSampleReq.getDbIp() + "'" +
                           " and db_type = '" + dbSampleReq.getDbType() + "'" +
                           " and db_name = '" + dbSampleReq.getDbName() + "'";
        Query query = new Query(influxSql);
        influxDB.query(query);
    }

    public void delTableHis(TableSampleReq tableSampleReq) {
        String influxSql = " delete from monitor_table_his" +
                           " where host_ip = '" + tableSampleReq.getHostIp() + "'" +
                           " and db_ip = '" + tableSampleReq.getDbIp() + "'" +
                           " and db_type = '" + tableSampleReq.getDbType() + "'" +
                           " and db_name = '" + tableSampleReq.getDbName() + "'" +
                           " and table_name = '" + tableSampleReq.getTableName() + "'";
        Query query = new Query(influxSql);
        influxDB.query(query);
    }
}
