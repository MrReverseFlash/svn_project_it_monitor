package com.envisioniot.uscada.monitor.web.controller;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.entity.FeInfo;
import com.envisioniot.uscada.monitor.web.entity.FeSampleReq;
import com.envisioniot.uscada.monitor.web.entity.FeSampleResp;
import com.envisioniot.uscada.monitor.web.service.FeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * FeController
 *
 * @author yangkang
 * @date 2021/1/28
 */
@RestController
@RequestMapping("/feInfo")
public class FeController {

    @Resource
    private FeService feService;

    @GetMapping("/list")
    public Response feInfoList() {
        List<FeInfo> feInfos = feService.qryAll();
        return new Response(ResponseCode.SUCCESS.getCode(), feInfos);
    }

    @PostMapping("/getFeSample")
    public Response getFeSample(@RequestBody FeSampleReq feSampleReq) {
        List<FeSampleResp> sample = feService.getFeSample(feSampleReq);
        return new Response(ResponseCode.SUCCESS.getCode(), sample);
    }
}
