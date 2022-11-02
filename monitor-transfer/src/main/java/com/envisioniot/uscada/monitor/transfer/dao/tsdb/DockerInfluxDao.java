package com.envisioniot.uscada.monitor.transfer.dao.tsdb;

import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.common.entity.ContainerStat;
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
public class DockerInfluxDao {

    @Autowired
    private InfluxDB influxdb;

    public void insertHis(String hostIp, List<ContainerStat> data) {
        try {
            data.forEach((container -> {
                String occurTime = container.getOccurTime();
                long timestamp = DateUtil.parse(occurTime, CommConstants.DEFAULT_TIME_FORMAT).getTime();
                Point.Builder builder = Point.measurement(INFLUX_DOCKER_HIS)
                        .time(timestamp, TimeUnit.MILLISECONDS)
                        .tag(TAG_IP, hostIp)
                        .tag(TAG_CONTAINER_ID, container.getId())
                        .addField(FIELD_STATUS, container.getStatus());
                if (container.getStatus() == CommConstants.ONLINE_STATUS) {
                    builder.addField(FIELD_CPU_PER, container.getCpuPer())
                            .addField(FIELD_MEM_PER, container.getMemPer())
                            .addField(FIELD_PIDS, container.getPids())
                            .addField(FIELD_NET_IO_SEND, container.getNetIoSend())
                            .addField(FIELD_NET_IO_RECEIVE, container.getNetIoReceive())
                            .addField(FIELD_BLOCK_IO_WRITE, container.getBlockIoWrite())
                            .addField(FIELD_BLOCK_IO_READ, container.getBlockIoRead())
                            .build();
                }
                Point point = builder.build();
                influxdb.write(point);
            }));
        } catch (Exception e) {
            log.error("host_ip={} docker container sample save in influxdb error.", hostIp);
            log.error(e.getMessage(), e);
        }
    }
}
