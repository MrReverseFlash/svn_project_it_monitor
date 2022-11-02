package com.envisioniot.uscada.monitor.web.controller;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.entity.DockerInfo;
import com.envisioniot.uscada.monitor.web.entity.DockerObj;
import com.envisioniot.uscada.monitor.web.entity.DockerSampleReq;
import com.envisioniot.uscada.monitor.web.entity.DockerSampleResp;
import com.envisioniot.uscada.monitor.web.service.DockerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * DockerController
 *
 * @author yangkang
 * @date 2021/1/22
 */
@RestController
@RequestMapping("/dockerInfo")
public class DockerController {

    @Resource
    private DockerService dockerService;

    @GetMapping("/list")
    public Response dockerInfoList() {
        List<DockerInfo> dockerInfos = dockerService.qryAll();
        return new Response(ResponseCode.SUCCESS.getCode(), dockerInfos);
    }

    @GetMapping("/query/{hostIp}")
    public Response agentDocker(@PathVariable("hostIp") String hostIp) {
        DockerObj results = dockerService.queryAgent(hostIp);
        return new Response(ResponseCode.SUCCESS.getCode(), results);
    }

    @PostMapping("/getDockerSample")
    public Response getDockerSample(@RequestBody DockerSampleReq dockerSampleReq) {
        List<DockerSampleResp> sample = dockerService.getDockerSample(dockerSampleReq);
        return new Response(ResponseCode.SUCCESS.getCode(), sample);
    }
}
