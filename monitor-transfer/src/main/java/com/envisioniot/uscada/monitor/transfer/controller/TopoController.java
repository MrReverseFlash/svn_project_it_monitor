package com.envisioniot.uscada.monitor.transfer.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.common.entity.TopoInfo;
import com.envisioniot.uscada.monitor.transfer.service.TopoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TopoController
 *
 * @author yangkang
 * @date 2021/3/1
 */
@RestController
@RequestMapping("/topo")
@Slf4j
public class TopoController {

    @Autowired
    private TopoService topoService;

    @GetMapping("/getInfo")
    public Response getMonitorObj(@RequestParam("hostIp") String hostIp) {
        if (StringUtils.isEmpty(hostIp)) {
            log.error("hostIP can not be empty!");
            return new Response(ResponseCode.FAIL.getCode(), "hostIP can not be empty!");
        }
        return new Response(ResponseCode.SUCCESS.getCode(), topoService.getMonitorRelaHosts(hostIp));
    }

    @PostMapping("/saveSample")
    public Response saveSample(@RequestParam(value = "isInit", required = false, defaultValue = "false") boolean isInit,
                               @RequestBody CommStat<TopoInfo> sample) {
        String hostIp = sample.getHostIp();
        if (StringUtils.isEmpty(hostIp)) {
            log.error("hostIP can not be empty!");
            return new Response(ResponseCode.FAIL.getCode(), "hostIP can not be empty!");
        }
        List<TopoInfo> sampleData = sample.getData();
        if (CollectionUtil.isEmpty(sampleData)) {
            log.error("host ip = {} topo initial data is empty.", hostIp);
            return new Response(ResponseCode.FAIL.getCode(), "Data of hostIP can not be empty!");
        }
        topoService.saveSample(sample, isInit);
        return new Response(ResponseCode.SUCCESS.getCode());
    }
}
