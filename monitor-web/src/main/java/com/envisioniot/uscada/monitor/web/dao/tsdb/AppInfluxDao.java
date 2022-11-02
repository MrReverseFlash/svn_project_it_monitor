package com.envisioniot.uscada.monitor.web.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.web.config.InfluxdbConfig;
import com.envisioniot.uscada.monitor.web.entity.AppSampleResp;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.querybuilder.time.DurationLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.envisioniot.uscada.monitor.common.util.InfluxConstants.*;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Repository
@Slf4j
public class AppInfluxDao {

    @Autowired
    private InfluxDB influxdb;

    @Autowired
    private InfluxdbConfig influxdbConfig;

    public void deleteAppHis(String appUid, String hostIp) {
        String matchTag = convEspCharacter(convertTag(appUid));
        String sql = String.format("delete from %s where %s = '%s' and %s='%s'",
                INFLUX_APP_HIS,
                TAG_IP, hostIp,
                TAG_APP_UID, matchTag);
        Query query = new Query(sql);
        influxdb.query(query);
        influxdb.flush();
    }

    public List<AppSampleResp> getSample(long st, long et, String hostIp, String appUid) {
        String matchTag = convEspCharacter(convertTag(appUid));
        Query query = select().column(INFLUX_TIME_FIELD)
                .column(FIELD_THREAD_NUM)
                .column(FIELD_CPU_PER)
                .column(FIELD_MEM_PER)
                .column(FIELD_MEM_USE)
                .column(FIELD_IO_READ)
                .column(FIELD_IO_WRITE)
                .from(influxdbConfig.getDatabase(), INFLUX_APP_HIS)
                .where(gte(INFLUX_TIME_FIELD, ti(st, DurationLiteral.MILLISECONDS)))
                .and(lte(INFLUX_TIME_FIELD, ti(et, DurationLiteral.MILLISECONDS)))
                .and(eq(TAG_IP, hostIp))
                // TODO appuid 有特殊字符需要处理
                .and(eq(TAG_APP_UID, matchTag))
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
        List<AppSampleResp> samples = new ArrayList<>(64);
        for (List<Object> item : values) {
            samples.add(new AppSampleResp(item));
        }
        return samples;
    }
}
