package com.envisioniot.uscada.monitor.transfer.controller;

import com.envisioniot.uscada.monitor.common.entity.HostStat;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.transfer.service.AlarmService;
import com.envisioniot.uscada.monitor.transfer.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hao.luo
 * @date 2020-12-25
 */
@RestController
@RequestMapping(value = "/host")
public class HostController {

    @Autowired
    private HostService hostService;

    @Autowired
    private AlarmService alarmService;

    @PostMapping("/saveSample")
    public Response saveSample(@RequestBody HostStat sample) {
        alarmService.geneCpuAlarm(sample);
        alarmService.geneMemAlarm(sample);
        alarmService.geneDiskAlarm(sample);
        alarmService.putHostIpCache(sample);
        hostService.saveSample(sample);
        return new Response(ResponseCode.SUCCESS.getCode());
    }
}
