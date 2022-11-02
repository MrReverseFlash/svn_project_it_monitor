package com.envisioniot.uscada.monitor.transfer.dao.tsdb;

import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.common.entity.HttpStat;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.envisioniot.uscada.monitor.common.util.InfluxConstants.*;

/**
 * @author hao.luo
 * @date 2020-12-28
 */
@Repository
@Slf4j
public class HttpInfluxDao {
    @Autowired
    private InfluxDB influxdb;

    public void insertHis(String hostIp, List<HttpStat> sampleData) {
        try {
            sampleData.forEach((port -> {
                String occurTime = port.getOccurTime();
                long timestamp = DateUtil.parse(occurTime, CommConstants.DEFAULT_TIME_FORMAT).getTime();
                Point.Builder builder = Point.measurement(INFLUX_HTTP_HIS)
                        .time(timestamp, TimeUnit.MILLISECONDS)
                        .tag(TAG_IP, hostIp)
                        .tag(TAG_HTTP_URL, port.getUrl())
                        .addField(FIELD_STATUS, port.getStatus())
                        .addField(FIELD_RESPONSE_CODE, port.getCode())
                        .addField(FIELD_RESPONSE_TIME, port.getResponseTime());
                Point point = builder.build();
                influxdb.write(point);
            }));
        } catch (Exception e) {
            log.error("host_ip={} http sample save in influxdb error.", hostIp);
            log.error(e.getMessage(), e);
        }
    }
}
