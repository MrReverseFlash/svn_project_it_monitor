package com.envisioniot.uscada.monitor.web.interceptor;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.exception.WebRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hao.luo
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Response handleTaskError(Exception e) {
        log.error(e.getMessage(), e);
        return new Response(ResponseCode.FAIL.getCode(), e.getMessage());
    }
}
