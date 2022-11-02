package com.envisioniot.uscada.monitor.common.entity;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
public enum HttpMethod {
    GET, POST;

    public static HttpMethod getMethod(String method) {
        for (HttpMethod value : HttpMethod.values()) {
            if (value.name().equalsIgnoreCase(method)) {
                return value;
            }
        }
        throw new IllegalArgumentException("http method = " + method + " not support.");
    }
}
