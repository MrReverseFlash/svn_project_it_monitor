package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * PortSampleReq
 *
 * @author yangkang
 * @date 2021/1/21
 */
@Data
public class PortSampleReq {
    private String st;
    private String et;
    private String hostIp;
    private String port;
}
