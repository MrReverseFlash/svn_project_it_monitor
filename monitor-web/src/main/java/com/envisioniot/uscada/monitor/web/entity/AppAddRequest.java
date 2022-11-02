package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
public class AppAddRequest {
    private String appUid;
    private String appPid;
    private String name;
    private String hostIp;
    private String containerName;
}
