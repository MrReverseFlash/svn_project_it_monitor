package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * TableSampleResp
 *
 * @author yangkang
 * @date 2021/1/21
 */
@Data
public class TableSampleResp {
    private String occurTime;
    private Long recordNum;

    public TableSampleResp(List<Object> influxData) {
        Instant instant = Instant.parse(String.valueOf(influxData.get(0)));
        this.occurTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.recordNum = influxData.get(1) != null ? ((Double) influxData.get(1)).longValue() : null;
    }
}
