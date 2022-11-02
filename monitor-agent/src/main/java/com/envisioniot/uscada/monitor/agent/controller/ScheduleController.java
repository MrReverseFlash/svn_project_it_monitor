package com.envisioniot.uscada.monitor.agent.controller;

import com.envisioniot.uscada.monitor.agent.schedule.ScheduledTaskService;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.common.entity.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-23
 */
@RestController
public class ScheduleController {

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @GetMapping("getAllTaskInfo")
    public Response getAllTaskInfo() {
        List<TaskInfo> allTask = scheduledTaskService.getAllTask();
        return new Response(ResponseCode.SUCCESS.getCode(), allTask);
    }

    @GetMapping("stop")
    public Response stop(@RequestParam("uuid") Integer uuid) {
        scheduledTaskService.stopTask(uuid);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("start")
    public Response start(@RequestParam("uuid") Integer uuid) {
        scheduledTaskService.startTask(uuid);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("restart")
    public Response restart(@RequestParam("uuid") Integer uuid) {
        scheduledTaskService.restartTask(uuid);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("setCron")
    public Response setCron(@RequestParam("uuid") Integer uuid,
                            @RequestParam("cron") String cron) {
        scheduledTaskService.modifyTask(uuid, cron);
        return new Response();
    }
}
