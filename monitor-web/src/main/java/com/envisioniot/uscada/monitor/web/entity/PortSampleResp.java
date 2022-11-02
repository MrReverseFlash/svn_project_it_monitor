package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * PortSampleResp
 *
 * @author yangkang
 * @date 2021/1/21
 */
@Data
public class PortSampleResp {
    private String occurTime;
    private Integer listeningNum;
    private Integer establishedNum;
    private Integer timewaitNum;
    private Integer closewaitNum;
    private Integer synsentNum;
    private Integer idleNum;
    private Integer status;

    public PortSampleResp(List<Object> influxData) {
        Instant instant = Instant.parse(String.valueOf(influxData.get(0)));
        this.occurTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.listeningNum = influxData.get(1) != null ? ((Double) influxData.get(1)).intValue() : null;
        this.establishedNum = influxData.get(2) != null ? ((Double) influxData.get(2)).intValue() : null;
        this.timewaitNum = influxData.get(3) != null ? ((Double) influxData.get(3)).intValue() : null;
        this.closewaitNum = influxData.get(4) != null ? ((Double) influxData.get(4)).intValue() : null;
        this.synsentNum = influxData.get(5) != null ? ((Double) influxData.get(5)).intValue() : null;
        this.idleNum = influxData.get(6) != null ? ((Double) influxData.get(6)).intValue() : null;
        this.status = influxData.get(7) != null ? ((Double) influxData.get(7)).intValue() : null;
    }
}
