package com.envisioniot.uscada.monitor.agent.exception;

import org.springframework.lang.Nullable;

/**
 * @author hao.luo
 * @date 2020-12-24
 */
public class TaskException extends IllegalArgumentException {

    private static final long serialVersionUID = 5275323981978842876L;

    public TaskException(String msg) {
        super(msg);
    }

    public TaskException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
