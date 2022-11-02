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
public class DiskSampleResponse {
    private String occurTime;
    private Double size;
    private Double used;
    private Double avail;
    private Double usePer;
    private Double inodesUsePer;
    private Double readSpeed;
    private Double writeSpeed;

    public DiskSampleResponse(List<Object> influxVal) {
        Instant parse = Instant.parse(String.valueOf(influxVal.get(0)));
        this.occurTime = parse.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.size = (Double) influxVal.get(1);
        this.used = (Double) influxVal.get(2);
        this.avail = (Double) influxVal.get(3);
        this.usePer = (Double) influxVal.get(4);
        this.inodesUsePer = (Double) influxVal.get(5);
        this.readSpeed = (Double) influxVal.get(6);
        this.writeSpeed = (Double) influxVal.get(7);
    }
}
