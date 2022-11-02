package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * PortInfo
 *
 * @author yangkang
 * @date 2021/1/21
 */
@Data
public class PortInfo {
    private Long id;
    private String hostIp;
    private String port;
    private String mark;
    private String portType;
    /**
     * 空：等待刷新；1：正常；2：断开；
     */
    private Integer status;
    private Integer listeningNum;
    private Integer establishedNum;
    private Integer timewaitNum;
    private Integer closewaitNum;
    private Integer synsentNum;
    private Integer idleNum;
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
