package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * DockerSampleResp
 *
 * @author yangkang
 * @date 2021/1/22
 */
@Data
public class DockerSampleResp {
    private String occurTime;
    private Double memPer;
    private Double cpuPer;
    private Long netIoSend;
    private Long netIoReceive;
    private Long blockIoRead;
    private Long blockIoWrite;
    private Integer pids;
    private Integer status;

    public DockerSampleResp(List<Object> influxData) {
        Instant instant = Instant.parse(String.valueOf(influxData.get(0)));
        this.occurTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.memPer = influxData.get(1) != null ? (Double) influxData.get(1) : null;
        this.cpuPer = influxData.get(2) != null ? (Double) influxData.get(2) : null;
        this.netIoSend = influxData.get(3) != null ? ((Double) influxData.get(3)).longValue() : null;
        this.netIoReceive = influxData.get(4) != null ? ((Double) influxData.get(4)).longValue() : null;
        this.blockIoRead = influxData.get(5) != null ? ((Double) influxData.get(5)).longValue() : null;
        this.blockIoWrite = influxData.get(6) != null ? ((Double) influxData.get(6)).longValue() : null;
        this.pids = influxData.get(7) != null ? ((Double) influxData.get(7)).intValue() : null;
        this.status = influxData.get(8) != null ? ((Double) influxData.get(8)).intValue() : null;
    }
}
