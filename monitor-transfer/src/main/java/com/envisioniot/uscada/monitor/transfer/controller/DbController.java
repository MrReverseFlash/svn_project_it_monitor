package com.envisioniot.uscada.monitor.transfer.controller;

import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.transfer.service.AlarmService;
import com.envisioniot.uscada.monitor.transfer.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-28
 */
@RestController
@RequestMapping(value = "/db")
public class DbController {
    @Autowired
    private DbService dbService;

    @Autowired
    private AlarmService alarmService;

    @PostMapping("/saveSample")
    public Response saveSample(@RequestParam(value = "isTable", required = false, defaultValue = "false") boolean isTable,
                               @RequestParam(value = "isInit", required = false, defaultValue = "false") boolean isInit,
                               @RequestBody CommStat<DbStat> sample) {
        if (isTable) {
            dbService.saveTableSample(sample, isInit);
        } else {
            if (!isInit) {
                alarmService.geneDbAlarm(sample);
            }
            dbService.saveDbSample(sample, isInit);
        }
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/getInfo")
    public Response getMonitorObj(@RequestParam("hostIp") String hostIp,
                                  @RequestParam(value = "isTable", required = false, defaultValue = "false") Boolean isTable) {
        List<DbStat> monitorObj = dbService.getMonitorObj(hostIp, isTable);
        return new Response(ResponseCode.SUCCESS.getCode(), monitorObj);
    }
}
