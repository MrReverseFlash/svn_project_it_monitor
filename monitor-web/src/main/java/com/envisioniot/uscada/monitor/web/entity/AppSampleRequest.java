package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
public class AppSampleRequest {
    private String st;
    private String et;
    private String appUid;
    private String hostIp;
    private String appName;
}
