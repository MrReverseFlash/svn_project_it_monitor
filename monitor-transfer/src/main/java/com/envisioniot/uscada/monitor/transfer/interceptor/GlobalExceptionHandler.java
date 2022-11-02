package com.envisioniot.uscada.monitor.transfer.interceptor;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.transfer.exception.DataProcessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hao.luo
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataProcessException.class)
    public Response handleTaskError(Exception e) {
        return new Response(ResponseCode.FAIL.getCode(), e.getMessage());
    }
}
