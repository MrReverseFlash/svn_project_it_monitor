package com.envisioniot.uscada.monitor.web.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.web.entity.DbSampleReq;
import com.envisioniot.uscada.monitor.web.entity.DbSampleResp;
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
 * PortInfluxDao
 *
 * @author yangkang
 * @date 2021/1/21
 */
@Repository
@Slf4j
public class PortInfluxDao {

    @Autowired
    private InfluxDB influxDB;

    public List<PortSampleResp> getPortSample(PortSampleReq portSampleReq) {
        String influxSql = " select time, listening_num, established_num, timewait_num, closewait_num, synsent_num, idle_num, status" +
                " from monitor_port_his" +
                " where time >= " + DateUtil.parse(portSampleReq.getSt()).getTime() + "ms" +
                " and time <= " + DateUtil.parse(portSampleReq.getEt()).getTime() + "ms" +
                " and host_ip = '" + portSampleReq.getHostIp() + "'" +
                " and port = '" + portSampleReq.getPort() + "'";
        Query query = new Query(influxSql);
        QueryResult result = influxDB.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        List<PortSampleResp> samples = new LinkedList<>();
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return samples;
        }
        List<List<Object>> values = results.get(0).getSeries().get(0).getValues();
        if (CollectionUtil.isEmpty(results)) {
            return samples;
        }
        for (List<Object> item : values) {
            samples.add(new PortSampleResp(item));
        }
        return samples;
    }

    public void delPortHis(PortSampleReq portSampleReq) {
        String influxSql = " delete from monitor_port_his" +
                " where host_ip = '" + portSampleReq.getHostIp() + "'" +
                " and port = '" + portSampleReq.getPort() + "'";
        Query query = new Query(influxSql);
        influxDB.query(query);
    }
}
