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
public class NetSampleResponse {
    private String occurTime;
    private Double totalBandWidth;
    private Double totalNetFlow;
    private Double totalNetIn;
    private Double totalNetOut;

    public NetSampleResponse(List<Object> influxVal) {
        Instant parse = Instant.parse(String.valueOf(influxVal.get(0)));
        this.occurTime = parse.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.totalBandWidth = (Double) influxVal.get(1);
        this.totalNetFlow = (Double) influxVal.get(2);
        this.totalNetIn = (Double) influxVal.get(3);
        this.totalNetOut = (Double) influxVal.get(4);
    }
}
