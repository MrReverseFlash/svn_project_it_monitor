package com.envisioniot.uscada.monitor.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Title: ConfigController</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/2/10 14:25
 */
@RestController
@Slf4j
public class ConfigController {

    @Value("${browserScadawebUrl:}")
    private String browserScadawebUrl;

    @Autowired
    private ConfigService configService;

    @GetMapping("/config")
    public Response config() {
        try {
            JSONObject frontConfig = configService.getFrontConfig();
            frontConfig.put("browserScadawebUrl", browserScadawebUrl);
            return new Response(ResponseCode.SUCCESS.getCode(), frontConfig);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Response(ResponseCode.FAIL.getCode(), e.getMessage());
        }
    }
}
