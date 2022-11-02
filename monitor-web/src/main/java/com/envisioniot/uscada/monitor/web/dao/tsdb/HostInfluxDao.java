package com.envisioniot.uscada.monitor.web.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.web.config.InfluxdbConfig;
import com.envisioniot.uscada.monitor.web.entity.DiskSampleResponse;
import com.envisioniot.uscada.monitor.web.entity.HostDetailResponse;
import com.envisioniot.uscada.monitor.web.entity.HostSampleResponse;
import com.envisioniot.uscada.monitor.web.entity.NetSampleResponse;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.querybuilder.time.DurationLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.envisioniot.uscada.monitor.common.util.InfluxConstants.*;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Repository
@Slf4j
public class HostInfluxDao {

    @Autowired
    private InfluxDB influxdb;

    @Autowired
    private InfluxdbConfig influxdbConfig;

    public List<HostSampleResponse> getHostSample(long st, long et, String hostIp) {
        Query query = select().column(INFLUX_TIME_FIELD)
                .column(FIELD_CPU_SYS)
                .column(FIELD_CPU_USER)
                .column(FIELD_CPU_IDLE)
                .column(FIELD_CPU_IOWAIT)
                .column(FIELD_CPU_IRQ)
                .column(FIELD_CPU_SOFT)
                .column(FIELD_MEM_TOTAL)
                .column(FIELD_MEM_USE)
                .column(FIELD_MEM_PER)
                .column(FIELD_MEM_FREE)
                .column(FIELD_SWAP_TOTAL)
                .column(FIELD_SWAP_USED)
                .column(FIELD_SWAP_PER)
                .column(FIELD_SWAP_PAGE_IN)
                .column(FIELD_SWAP_PAGE_OUT)
                .column(FIELD_PROCESS_NUM)
                .from(influxdbConfig.getDatabase(), INFLUX_HOST_HIS)
                .where(gte(INFLUX_TIME_FIELD, ti(st, DurationLiteral.MILLISECONDS)))
                .and(lte(INFLUX_TIME_FIELD, ti(et, DurationLiteral.MILLISECONDS)))
                .and(eq(TAG_IP, hostIp))
                .orderBy(asc());
        QueryResult result = influxdb.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return null;
        }
        List<List<Object>> values = results.get(0).getSeries().get(0).getValues();
        if (CollectionUtil.isEmpty(results)) {
            return null;
        }
        List<HostSampleResponse> samples = new ArrayList<>(64);
        for (List<Object> item : values) {
            samples.add(new HostSampleResponse(item));
        }
        return samples;
    }

    public List<HostDetailResponse> getHostDetail(long st, long et, String hostIp) {
        Query query = select().column(INFLUX_TIME_FIELD)
                .column(FIELD_CPU_SYS)
                .column(FIELD_CPU_USER)
                .column(FIELD_CPU_IDLE)
                .column(FIELD_CPU_IOWAIT)
                .column(FIELD_CPU_IRQ)
                .column(FIELD_CPU_SOFT)
                .column(FIELD_MEM_TOTAL)
                .column(FIELD_MEM_USE)
                .column(FIELD_MEM_PER)
                .column(FIELD_MEM_FREE)
                .column(FIELD_ONE_LOAD)
                .column(FIELD_FIVE_LOAD)
                .column(FIELD_FIFTEEN_LOAD)
                .column(FIELD_RXBYT)
                .column(FIELD_TXBYT)
                .column(FIELD_RXPCK)
                .column(FIELD_TXPCK)
                .column(FIELD_RXERRORS)
                .column(FIELD_TXERRORS)
                .column(FIELD_RXDROPS)
                .column(FIELD_TXDROPS)
                .from(influxdbConfig.getDatabase(), INFLUX_HOST_HIS)
                .where(gte(INFLUX_TIME_FIELD, ti(st, DurationLiteral.MILLISECONDS)))
                .and(lte(INFLUX_TIME_FIELD, ti(et, DurationLiteral.MILLISECONDS)))
                .and(eq(TAG_IP, hostIp))
                .orderBy(asc());
        QueryResult result = influxdb.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return null;
        }
        List<List<Object>> values = results.get(0).getSeries().get(0).getValues();
        if (CollectionUtil.isEmpty(results)) {
            return null;
        }
        List<HostDetailResponse> samples = new ArrayList<>(64);
        for (List<Object> item : values) {
            samples.add(new HostDetailResponse(item));
        }
        return samples;
    }

    public Map<String, List<NetSampleResponse>> getNetSample(long st, long et, String hostIp) {
        Query query = select().column(INFLUX_TIME_FIELD)
                .column(FIELD_TOTAL_BANDWIDTH)
                .column(FIELD_TOTAL_NETFLOW)
                .column(FIELD_TOTAL_NET_IN)
                .column(FIELD_TOTAL_NET_OUT)
                .from(influxdbConfig.getDatabase(), INFLUX_NET_HIS)
                .where(gte(INFLUX_TIME_FIELD, ti(st, DurationLiteral.MILLISECONDS)))
                .and(lte(INFLUX_TIME_FIELD, ti(et, DurationLiteral.MILLISECONDS)))
                .and(eq(TAG_IP, hostIp))
                .groupBy(TAG_NET_CARD_NAME)
                .orderBy(asc());
        QueryResult result = influxdb.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return null;
        }
        Map<String, List<NetSampleResponse>> samples = new HashMap<>(4);
        List<QueryResult.Series> series = results.get(0).getSeries();
        series.forEach((item) -> {
            String cardName = item.getTags().get(TAG_NET_CARD_NAME);
            List<List<Object>> values = item.getValues();
            if(CollectionUtil.isEmpty(values)){
                samples.put(cardName, null);
                return;
            }
            List<NetSampleResponse> netList = new ArrayList<>(8);
            for (List<Object> val : values) {
                netList.add(new NetSampleResponse(val));
            }
            samples.put(cardName, netList);
        });
        return samples;
    }

    public Map<String, List<DiskSampleResponse>> getDiskSample(long st, long et, String hostIp) {
        Query query = select().column(INFLUX_TIME_FIELD)
                .column(FIELD_SIZE)
                .column(FIELD_USED)
                .column(FIELD_AVAIL)
                .column(FIELD_USE_PER)
                .column(FIELD_INODES_PER)
                .column(FIELD_READ_RATE)
                .column(FIELD_WRITE_RATE)
                .from(influxdbConfig.getDatabase(), INFLUX_DISK_HIS)
                .where(gte(INFLUX_TIME_FIELD, ti(st, DurationLiteral.MILLISECONDS)))
                .and(lte(INFLUX_TIME_FIELD, ti(et, DurationLiteral.MILLISECONDS)))
                .and(eq(TAG_IP, hostIp))
                .groupBy(TAG_DISK_NAME)
                .orderBy(asc());
        QueryResult result = influxdb.query(query);
        List<QueryResult.Result> results = result.getResults();
        if (log.isDebugEnabled()) {
            log.debug("influxdb sql={}", query.getCommand());
        }
        if (CollectionUtil.isEmpty(results) || CollectionUtil.isEmpty(results.get(0).getSeries())) {
            log.warn("there is no data from sql={}", query.getCommand());
            return null;
        }
        Map<String, List<DiskSampleResponse>> samples = new HashMap<>(4);
        List<QueryResult.Series> series = results.get(0).getSeries();
        series.forEach((item) -> {
            String diskName = item.getTags().get(TAG_DISK_NAME);
            List<List<Object>> values = item.getValues();
            if(CollectionUtil.isEmpty(values)){
                samples.put(diskName, null);
                return;
            }
            List<DiskSampleResponse> netList = new ArrayList<>(8);
            for (List<Object> val : values) {
                netList.add(new DiskSampleResponse(val));
            }
            samples.put(diskName, netList);
        });
        return samples;
    }
}
