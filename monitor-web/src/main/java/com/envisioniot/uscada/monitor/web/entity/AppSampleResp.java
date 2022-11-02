package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
@NoArgsConstructor
public class AppSampleResp {
    private String occurTime;
    private Integer threadNum;
    private Double cpuPer;
    private Double memPer;
    private Double memUse;
    private Long diskIoRead;
    private Long diskIoWritten;

    public AppSampleResp(List<Object> influxVal) {
        Instant parse = Instant.parse(String.valueOf(influxVal.get(0)));
        this.occurTime = parse.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.threadNum = influxVal.get(1) != null ? ((Double) influxVal.get(1)).intValue() : null;
        this.cpuPer = (Double) influxVal.get(2);
        this.memPer = (Double) influxVal.get(3);
        this.memUse = (Double) influxVal.get(4);
        this.diskIoRead = influxVal.get(5) != null ? ((Double) influxVal.get(5)).longValue() : null;
        this.diskIoWritten = influxVal.get(6) != null ? ((Double) influxVal.get(6)).longValue() : null;
    }
}
