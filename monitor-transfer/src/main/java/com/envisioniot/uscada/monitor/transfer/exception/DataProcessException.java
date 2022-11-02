package com.envisioniot.uscada.monitor.transfer.exception;

import org.springframework.lang.Nullable;

/**
 * @author hao.luo
 * @date 2020-12-25
 */
public class DataProcessException extends RuntimeException {

    private static final long serialVersionUID = 1985598098753403927L;

    public DataProcessException(String msg) {
        super(msg);
    }

    public DataProcessException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
