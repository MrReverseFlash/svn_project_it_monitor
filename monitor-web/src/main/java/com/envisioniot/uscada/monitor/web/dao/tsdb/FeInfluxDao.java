package com.envisioniot.uscada.monitor.web.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.web.entity.DockerSampleResp;
import com.envisioniot.uscada.monitor.web.entity.FeSampleReq;
import com.envisioniot.uscada.monitor.web.entity.FeSampleResp;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

/**
 * FeInfluxDao
 *
 * @author yangkang
 * @date 2021/1/27
 */
@Repository
@Slf4j
public class FeInfluxDao {

    @Autowired
    private InfluxDB influxDB;

    public List<FeSampleResp> getFeSample(FeSampleReq feSampleReq) {
        String influxSql = " select time, status" +
                " from monitor_ied_his" +
                " where time >= " + DateUtil.parse(feSampleReq.getSt()).getTime() + "ms" +
                " and time <= " + DateUtil.parse(feSampleReq.getEt()).getTime() + "ms" +
                " and host_ip = '" + feSampleReq.getHostIp() + "'" +
                " and peer_ip = '" + feSampleReq.getPeerIp() + "'" +
                " and port = '" + feSampleReq.getPort() + "'";
        Query query = new Query(influxSql);
        QueryResult result = influxDB.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        List<FeSampleResp> samples = new LinkedList<>();
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return samples;
        }
        List<List<Object>> values = results.get(0).getSeries().get(0).getValues();
        if (CollectionUtil.isEmpty(results)) {
            return samples;
        }
        for (List<Object> item : values) {
            samples.add(new FeSampleResp(item));
        }
        return samples;
    }
}
