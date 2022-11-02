package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * TopoRela
 *
 * @author yangkang
 * @date 2021/3/3
 */
@Data
public class TopoRela {
    private String hostIpTarget;
    private Short status;
    private Short operationMillis;
    private Short isDefault;
    private String updateTime;
    private String createTime;
}
