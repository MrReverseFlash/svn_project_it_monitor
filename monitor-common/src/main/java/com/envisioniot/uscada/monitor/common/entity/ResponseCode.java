package com.envisioniot.uscada.monitor.common.entity;

/**
 * @author hao.luo
 */
public enum ResponseCode {
    SUCCESS(10000),
    FAIL(20000),
    NOLOGIN(30000),
    ;

    ResponseCode(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
