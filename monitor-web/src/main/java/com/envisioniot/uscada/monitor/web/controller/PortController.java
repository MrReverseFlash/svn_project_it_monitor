package com.envisioniot.uscada.monitor.web.controller;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.common.util.NumberUtil;
import com.envisioniot.uscada.monitor.web.entity.PortInfo;
import com.envisioniot.uscada.monitor.web.entity.PortSampleReq;
import com.envisioniot.uscada.monitor.web.entity.PortSampleResp;
import com.envisioniot.uscada.monitor.web.service.PortService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PortController
 *
 * @author yangkang
 * @date 2021/1/21
 */
@RestController
@RequestMapping("/portInfo")
public class PortController {

    @Resource
    private PortService portService;

    @GetMapping("/list")
    public Response portInfoList() {
        List<PortInfo> portInfos = portService.qryAll();
        return new Response(ResponseCode.SUCCESS.getCode(), portInfos);
    }


    @PostMapping("/save")
    public Response savePortInfo(@RequestBody PortInfo portInfo) {
        if (portInfo.getId() == null) {
            boolean validPort = NumberUtil.isValidPort(portInfo.getPort());
            if (!validPort) {
                return new Response(ResponseCode.FAIL.getCode(), "invalid parameter port, pls check!");
            }
            portService.save(portInfo);
        } else {
            portService.updateById(portInfo);
        }
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("del")
    public Response delete(@RequestBody List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            portService.deleteByIds(ids);
        }
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/getPortSample")
    public Response getPortSample(@RequestBody PortSampleReq portSampleReq) {
        List<PortSampleResp> sample = portService.getPortSample(portSampleReq);
        return new Response(ResponseCode.SUCCESS.getCode(), sample);
    }
}
