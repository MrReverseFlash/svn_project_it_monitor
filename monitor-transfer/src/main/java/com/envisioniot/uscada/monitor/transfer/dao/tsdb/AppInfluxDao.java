package com.envisioniot.uscada.monitor.transfer.dao.tsdb;

import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.common.entity.*;
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
 * @date 2020-12-25
 */
@Repository
@Slf4j
public class AppInfluxDao {
    @Autowired
    private InfluxDB influxdb;

    public void insertHis(String hostIp, List<AppStat> sampleData) {
        try {
            sampleData.forEach((app -> {
                String occurTime = app.getOccurTime();
                long timestamp = DateUtil.parse(occurTime, CommConstants.DEFAULT_TIME_FORMAT).getTime();
                Point.Builder builder = Point.measurement(INFLUX_APP_HIS)
                        .time(timestamp, TimeUnit.MILLISECONDS)
                        .tag(TAG_IP, hostIp)
                        .tag(TAG_APP_UID, convertTag(app.getAppUid()))
                        .addField(FIELD_STATUS, app.getStatus());
                if (app.getStatus() == CommConstants.ONLINE_STATUS) {
                    builder.addField(FIELD_MEM_USE, app.getMemUse())
                            .addField(FIELD_MEM_PER, app.getMemPer())
                            .addField(FIELD_CPU_PER, app.getCpuPer())
                            .addField(FIELD_IO_READ, app.getDiskIoRead())
                            .addField(FIELD_IO_WRITE, app.getDiskIoWritten())
                            .addField(FIELD_APP_PID, app.getAppPid())
                            .addField(FIELD_USER, app.getUser())
                            .addField(FIELD_THREAD_NUM, app.getThreadNum());
                }
                Point point = builder.build();
                influxdb.write(point);
            }));
        } catch (Exception e) {
            log.error("host_ip={} app sample save in influxdb error.", hostIp);
            log.error(e.getMessage(), e);
        }
    }
}
