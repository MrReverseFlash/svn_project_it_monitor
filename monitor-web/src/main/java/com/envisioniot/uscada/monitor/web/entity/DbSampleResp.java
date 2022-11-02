package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * DbSampleResp
 *
 * @author yangkang
 * @date 2021/1/20
 */
@Data
public class DbSampleResp {
    private String occurTime;
    private Integer commitNum;
    private Integer connMax;
    private Integer useConn;
    private Integer questions;
    private Integer rollbackNum;
    private Integer status;

    public DbSampleResp(List<Object> influxData) {
        Instant instant = Instant.parse(String.valueOf(influxData.get(0)));
        this.occurTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.commitNum = influxData.get(1) != null ? ((Double) influxData.get(1)).intValue() : null;
        this.connMax = influxData.get(2) != null ? ((Double) influxData.get(2)).intValue() : null;
        this.useConn = influxData.get(3) != null ? ((Double) influxData.get(3)).intValue() : null;
        this.questions = influxData.get(4) != null ? ((Double) influxData.get(4)).intValue() : null;
        this.rollbackNum = influxData.get(5) != null ? ((Double) influxData.get(5)).intValue() : null;
        this.status = influxData.get(6) != null ? ((Double) influxData.get(6)).intValue() : null;
    }
}
