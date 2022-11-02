package com.envisioniot.uscada.monitor.transfer.controller;

import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.transfer.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-25
 */
@RestController
@RequestMapping(value = "/app")
public class AppController {
    @Autowired
    private AppService appService;

    @PostMapping("/saveSample")
    public Response saveSample(@RequestParam(value = "isInit", required = false, defaultValue = "false") boolean isInit,
                               @RequestBody CommStat<AppStat> sample) {
        appService.saveSample(sample, isInit);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/getInfo")
    public Response getMonitorObj(@RequestParam("hostIp") String hostIp){
        List<AppObj> monitorObj = appService.getMonitorObj(hostIp);
        return new Response(ResponseCode.SUCCESS.getCode(), monitorObj);
    }
}
