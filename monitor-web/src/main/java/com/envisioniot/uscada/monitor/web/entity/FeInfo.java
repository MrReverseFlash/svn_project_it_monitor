package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * FeInfo
 *
 * @author yangkang
 * @date 2021/1/27
 */
@Data
public class FeInfo {
    private Long id;
    private String hostIp;
    private String iedAlias;
    private String iedName;
    private String peerIp;
    private String port;
    private Integer commType;
    /**
     * 空：等待刷新；1：正常；2：断开；
     */
    private Integer status;
    private String msg;
    private String createTime;
    private String updateTime;
    /**
     * 主机状态，1：正常，2：下线
     */
    private Integer hostStatus;
}
