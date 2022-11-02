package com.envisioniot.uscada.monitor.web.exception;

import org.springframework.lang.Nullable;

/**
 * @author hao.luo
 * @date 2020-12-25
 */
public class WebRequestException extends RuntimeException {

    private static final long serialVersionUID = 1985598098753403927L;

    public WebRequestException(String msg) {
        super(msg);
    }

    public WebRequestException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
