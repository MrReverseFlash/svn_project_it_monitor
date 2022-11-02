package com.envisioniot.uscada.monitor.transfer.dao.tsdb;

import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.common.entity.PortStatSample;
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
public class PortInfluxDao {
    @Autowired
    private InfluxDB influxdb;

    public void insertHis(String hostIp, List<PortStatSample> sampleData) {
        try {
            sampleData.forEach((port -> {
                String occurTime = port.getOccurTime();
                long timestamp = DateUtil.parse(occurTime, CommConstants.DEFAULT_TIME_FORMAT).getTime();
                Point.Builder builder = Point.measurement(INFLUX_PORT_HIS)
                        .time(timestamp, TimeUnit.MILLISECONDS)
                        .tag(TAG_IP, hostIp)
                        .tag(TAG_PORT, port.getPortNum())
                        .addField(FIELD_STATUS, port.getStatus());
                builder.addField(FIELD_LISTENING_NUM, port.getListeningNum())
                        .addField(FIELD_ESTABLISHED_NUM, port.getEstablishedNum())
                        .addField(FIELD_TIMEWAIT_NUM, port.getTimeWaitNum())
                        .addField(FIELD_CLOSEWAIT_NUM, port.getCloseWaitNum())
                        .addField(FIELD_SYNSENT_NUM, port.getSynSentNum())
                        .addField(FIELD_IDLE_NUM, port.getIdleNum());
                Point point = builder.build();
                influxdb.write(point);
            }));
        } catch (Exception e) {
            log.error("host_ip={} port sample save in influxdb error.", hostIp);
            log.error(e.getMessage(), e);
        }
    }
}
