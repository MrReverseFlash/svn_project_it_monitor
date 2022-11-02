package com.envisioniot.uscada.monitor.agent.interceptor;

import com.envisioniot.uscada.monitor.agent.exception.TaskException;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hao.luo
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskException.class)
    public Response handleTaskError(Exception e) {
        return new Response(ResponseCode.FAIL.getCode(), e.getMessage());
    }
}
