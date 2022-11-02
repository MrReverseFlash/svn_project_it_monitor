package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * GatewayInfo
 *
 * @author yangkang
 * @date 2021/3/3
 */
@Data
public class GatewayInfo {
    private String gatewayIp;
    private Short status;
    private Short operationMillis;
    private String updateTime;
    private String createTime;
}
