package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * HttpInfo
 *
 * @author yangkang
 * @date 2021/1/28
 */
@Data
public class HttpInfo {
    private Long id;
    private String hostIp;
    private String url;
    private String serviceName;
    private String method;
    private String body;
    /**
     * 空：等待刷新；1：正常；2：断开；
     */
    private Integer status;
    private Integer responseCode;
    private Integer responseTime;
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
