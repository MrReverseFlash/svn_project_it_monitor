package com.envisioniot.uscada.monitor.agent.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VersionController
 *
 * @author yangkang
 * @date 2021/1/26
 */
@RestController
public class VersionController {

    @Value("${version}")
    private String version;

    @GetMapping("version")
    public String getVersion(){
        return version;
    }
}
