package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
@Data
public class HttpInfo {
    /**
     * 目前仅支持GET、POST
     */
    private String method;
    /**
     * 监控服务名称
     */
    private String appName;
    /**
     * 需要监控的URL
     */
    private String url;
    /**
     * POST 数据，可选
     */
    private String body;
}
