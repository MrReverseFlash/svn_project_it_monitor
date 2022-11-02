package com.envisioniot.uscada.monitor.web.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.web.entity.HttpSampleReq;
import com.envisioniot.uscada.monitor.web.entity.HttpSampleResp;
import com.envisioniot.uscada.monitor.web.entity.PortSampleReq;
import com.envisioniot.uscada.monitor.web.entity.PortSampleResp;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

/**
 * HttpInfluxDao
 *
 * @author yangkang
 * @date 2021/1/28
 */
@Repository
@Slf4j
public class HttpInfluxDao {

    @Autowired
    private InfluxDB influxDB;

    public List<HttpSampleResp> getHttpSample(HttpSampleReq httpSampleReq) {
        String influxSql = " select time, response_code, response_time, status" +
                " from monitor_http_his" +
                " where time >= " + DateUtil.parse(httpSampleReq.getSt()).getTime() + "ms" +
                " and time <= " + DateUtil.parse(httpSampleReq.getEt()).getTime() + "ms" +
                " and host_ip = '" + httpSampleReq.getHostIp() + "'" +
                " and url = '" + httpSampleReq.getUrl() + "'";
        Query query = new Query(influxSql);
        QueryResult result = influxDB.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        List<HttpSampleResp> samples = new LinkedList<>();
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return samples;
        }
        List<List<Object>> values = results.get(0).getSeries().get(0).getValues();
        if (CollectionUtil.isEmpty(results)) {
            return samples;
        }
        for (List<Object> item : values) {
            samples.add(new HttpSampleResp(item));
        }
        return samples;
    }

    public void delHttpHis(HttpSampleReq httpSampleReq) {
        String influxSql = " delete from monitor_http_his" +
                " where host_ip = '" + httpSampleReq.getHostIp() + "'" +
                " and url = '" + httpSampleReq.getUrl() + "'";
        Query query = new Query(influxSql);
        influxDB.query(query);
    }
}
