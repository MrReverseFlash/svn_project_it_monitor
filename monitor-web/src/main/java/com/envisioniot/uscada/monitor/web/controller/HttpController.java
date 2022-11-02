package com.envisioniot.uscada.monitor.web.controller;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.entity.*;
import com.envisioniot.uscada.monitor.web.service.HttpService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * HttpController
 *
 * @author yangkang
 * @date 2021/1/28
 */
@RestController
@RequestMapping("/httpInfo")
public class HttpController {

    @Resource
    private HttpService httpService;

    @GetMapping("/list")
    public Response httpInfoList() {
        List<HttpInfo> httpInfos = httpService.qryAll();
        return new Response(ResponseCode.SUCCESS.getCode(), httpInfos);

    }

    @PostMapping("/save")
    public Response saveHttpInfo(@RequestBody HttpInfo httpInfo) {
        if (httpInfo.getId() == null) {
            httpService.save(httpInfo);
        } else {
            httpService.updateById(httpInfo);
        }
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("del")
    public Response delete(@RequestBody List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            httpService.deleteByIds(ids);
        }
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/getHttpSample")
    public Response getHttpSample(@RequestBody HttpSampleReq httpSampleReq) {
        List<HttpSampleResp> sample = httpService.getHttpSample(httpSampleReq);
        return new Response(ResponseCode.SUCCESS.getCode(), sample);
    }
}
