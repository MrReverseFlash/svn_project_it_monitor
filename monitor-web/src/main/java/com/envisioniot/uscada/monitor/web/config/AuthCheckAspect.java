package com.envisioniot.uscada.monitor.web.config;

import com.alibaba.fastjson.JSONObject;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.util.UscadaClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

/**
 * AuthCheckAspect
 * 认证校验切面
 *
 * @author yangkang
 * @date 2020/4/13
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class AuthCheckAspect {

    private HashSet<String> excludePathSet = new HashSet<>(8);

    {
        excludePathSet.add("/config");
        excludePathSet.add("/version");
    }

    @Value("${uscadaIp:}")
    private String uscadaIp;

    @Value("${browserScadawebUrl:}")
    private String browserScadawebUrl;

    @Pointcut("execution(@org.springframework.web.bind.annotation.RequestMapping public com.envisioniot.uscada.monitor.common.entity.Response || Object || String com.envisioniot.uscada.monitor.web.controller.*.*(..)) ||" +
              "execution(@(@org.springframework.web.bind.annotation.RequestMapping *) public com.envisioniot.uscada.monitor.common.entity.Response || Object || String com.envisioniot.uscada.monitor.web.controller.*.*(..))")
    public void authCheck(){}

    @Around("authCheck()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        //不能仅仅通过@GetMapping或者@PostMapping注解里的value值来筛选。如：/1/test, /2/test, 不能只去筛选/test。
//        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(((MethodSignature) pjp.getSignature()).getMethod(), RequestMapping.class);
//        if (excludePathSet.contains(((String[]) annotationAttributes.get("value"))[0])) {
//            return pjp.proceed();
//        }

        Response resp = new Response();
        if (!checkSession(resp)) {
            return resp;
        }
        return pjp.proceed();
    }

    private boolean checkSession(Response resp) {
        //若USCADA的IP配置为空，则不校验session
        if (StringUtils.isBlank(browserScadawebUrl) || StringUtils.isBlank(uscadaIp)) {
            return true;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //筛选掉放开的路径
        String servletPath = request.getServletPath();
        if (excludePathSet.contains(servletPath)) {
            return true;
        }
        //去USCADA校验session
        Object sessionId = request.getHeader("authToken");
        String msg = "Check session failed";
        if (sessionId != null) {
            String sessionIdStr = String.valueOf(sessionId);
            if (StringUtils.isNotBlank(sessionIdStr)) {
                try {
                    JSONObject respJson = UscadaClient.checkSession(uscadaIp, sessionIdStr);
                    if (respJson != null) {
                        if ("10000".equals(respJson.getString("code"))) {
                            return true;
                        } else {
                            msg = respJson.getString("message");
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    msg = e.getMessage();
                }
            }
        }
        resp.setCode(ResponseCode.NOLOGIN.getCode());
        resp.setMsg(msg);
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        return false;
    }
}
