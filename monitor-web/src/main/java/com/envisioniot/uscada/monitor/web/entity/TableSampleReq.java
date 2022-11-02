package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * TableSampleReq
 *
 * @author yangkang
 * @date 2021/1/21
 */
@Data
public class TableSampleReq {
    private String st;
    private String et;
    private String hostIp;
    private String dbIp;
    private String dbType;
    private String dbName;
    private String tableName;
}
