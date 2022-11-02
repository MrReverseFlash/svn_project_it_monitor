package com.envisioniot.uscada.monitor.transfer.dao.tsdb;

import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.common.entity.IedStat;
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
public class IedInfluxDao {
    @Autowired
    private InfluxDB influxdb;

    public void insertHis(String hostIp, List<IedStat> sampleData) {
        try {
            sampleData.forEach((ied -> {
                String occurTime = ied.getOccurTime();
                long timestamp = DateUtil.parse(occurTime, CommConstants.DEFAULT_TIME_FORMAT).getTime();
                Point point = Point.measurement(INFLUX_IED_HIS)
                        .time(timestamp, TimeUnit.MILLISECONDS)
                        .tag(TAG_IP, hostIp)
                        .tag(TAG_PEER_IP, ied.getPeerIp())
                        .tag(TAG_PORT, ied.getPort())
                        .addField(FIELD_STATUS, ied.getStatus()).build();
                influxdb.write(point);
            }));
        } catch (Exception e) {
            log.error("host_ip={} ied sample save in influxdb error.", hostIp);
            log.error(e.getMessage(), e);
        }
    }
}
