package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.common.util.NumberUtil;
import com.envisioniot.uscada.monitor.web.util.Constants;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * FeSampleResp
 *
 * @author yangkang
 * @date 2021/1/27
 */
@Data
public class FeSampleResp {
    private String occurTime;
    private Integer status;

    public FeSampleResp(List<Object> influxData) {
        Instant instant = Instant.parse(String.valueOf(influxData.get(0)));
        this.occurTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(Constants.SECOND_DF);
        this.status = NumberUtil.toInteger(influxData.get(1));
    }
}
