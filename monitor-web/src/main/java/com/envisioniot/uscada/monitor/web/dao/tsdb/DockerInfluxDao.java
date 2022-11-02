package com.envisioniot.uscada.monitor.web.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.web.entity.DockerSampleReq;
import com.envisioniot.uscada.monitor.web.entity.DockerSampleResp;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

/**
 * DockerInfluxDao
 *
 * @author yangkang
 * @date 2021/1/22
 */
@Repository
@Slf4j
public class DockerInfluxDao {

    @Autowired
    private InfluxDB influxDB;

    public List<DockerSampleResp> getDockerSample(DockerSampleReq dockerSampleReq) {
        String influxSql = " select time, mem_per, cpu_per, net_io_send, net_io_receive, block_io_read, block_io_write, pids, status" +
                " from monitor_docker_his" +
                " where time >= " + DateUtil.parse(dockerSampleReq.getSt()).getTime() + "ms" +
                " and time <= " + DateUtil.parse(dockerSampleReq.getEt()).getTime() + "ms" +
                " and host_ip = '" + dockerSampleReq.getHostIp() + "'" +
                " and container_id = '" + dockerSampleReq.getContainerId() + "'";
        Query query = new Query(influxSql);
        QueryResult result = influxDB.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        List<DockerSampleResp> samples = new LinkedList<>();
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return samples;
        }
        List<List<Object>> values = results.get(0).getSeries().get(0).getValues();
        if (CollectionUtil.isEmpty(results)) {
            return samples;
        }
        for (List<Object> item : values) {
            samples.add(new DockerSampleResp(item));
        }
        return samples;
    }
}
