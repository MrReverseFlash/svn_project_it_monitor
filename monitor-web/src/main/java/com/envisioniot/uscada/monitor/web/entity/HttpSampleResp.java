package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.common.util.NumberUtil;
import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * HttpSampleResp
 *
 * @author yangkang
 * @date 2021/1/28
 */
@Data
public class HttpSampleResp {
    private String occurTime;
    private Integer responseCode;
    private Integer responseTime;
    private Integer status;

    public HttpSampleResp(List<Object> influxData) {
        Instant instant = Instant.parse(String.valueOf(influxData.get(0)));
        this.occurTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.responseCode = NumberUtil.toInteger(influxData.get(1));
        this.responseTime = NumberUtil.toInteger(influxData.get(2));
        this.status = NumberUtil.toInteger(influxData.get(3));
    }
}
