package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * FeSampleReq
 *
 * @author yangkang
 * @date 2021/1/27
 */
@Data
public class FeSampleReq {
    private String st;
    private String et;
    private String hostIp;
    private String peerIp;
    private String port;
}
