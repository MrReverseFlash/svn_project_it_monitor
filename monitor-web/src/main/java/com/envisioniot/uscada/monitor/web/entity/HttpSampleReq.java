package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * HttpSampleReq
 *
 * @author yangkang
 * @date 2021/1/28
 */
@Data
public class HttpSampleReq {
    private String st;
    private String et;
    private String hostIp;
    private String url;
}
