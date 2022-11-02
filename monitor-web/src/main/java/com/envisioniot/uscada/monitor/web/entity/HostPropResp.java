package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
public class HostPropResp {
    private String hostIp;
    private String port;
    private String label;
    private Integer type;
}
