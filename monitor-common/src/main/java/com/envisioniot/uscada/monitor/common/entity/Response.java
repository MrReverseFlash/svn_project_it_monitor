package com.envisioniot.uscada.monitor.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hao.luo
 */
@Getter
@Setter
public class Response {
    private int code;
    private String msg;
    private Object data;

    public Response() {
    }

    public Response(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code) {
        this.code = code;
    }
}
