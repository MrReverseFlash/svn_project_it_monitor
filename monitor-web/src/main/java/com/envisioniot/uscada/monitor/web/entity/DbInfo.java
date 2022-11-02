package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
@Data
public class DbInfo {
    private String id;
    private String dbName;
    private String dbType;
    private String user;
    private String passwd;
    private String dbIp;
    private String dbPort;
    private String version;
    /**
     * 空：等待刷新；1：正常；2：断开；
     */
    private Integer status;
    private String hostIp;
    private String dbTag;
    private Long upTime;
    private Long questions;
    private Long commitNum;
    private Long rollbackNum;
    private Long connMax;
    private Long useCon;
    private String msg;
    private String createTime;
    private String updateTime;
    /**
     * 主机状态，1：正常，2：下线
     */
    private Integer hostStatus;

    /**
     * 是否属于本区域监控主机
     */
    private Boolean hostMatch;

    private String matchFlag;
}
