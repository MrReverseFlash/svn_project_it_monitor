package com.envisioniot.uscada.monitor.transfer.controller;

import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.transfer.service.DockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-28
 */
@RestController
@RequestMapping(value = "/docker")
public class DockerController {
    @Autowired
    private DockerService dockerService;

    @PostMapping("/saveSample")
    public Response saveSample(@RequestParam(value = "isInit", required = false, defaultValue = "false") boolean isInit,
                               @RequestBody CommStat<ContainerStat> sample) {
        dockerService.saveSample(sample, isInit);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/getInfo")
    public Response getMonitorObj(@RequestParam("hostIp") String hostIp) {
        List<String> monitorObj = dockerService.getMonitorContainerIds(hostIp);
        return new Response(ResponseCode.SUCCESS.getCode(), monitorObj);
    }
}
