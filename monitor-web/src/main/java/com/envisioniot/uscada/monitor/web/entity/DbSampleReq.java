package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * DbSampleReq
 *
 * @author yangkang
 * @date 2021/1/20
 */
@Data
public class DbSampleReq {
    private String st;
    private String et;
    private String hostIp;
    private String dbIp;
    private String dbType;
    private String dbName;
}
