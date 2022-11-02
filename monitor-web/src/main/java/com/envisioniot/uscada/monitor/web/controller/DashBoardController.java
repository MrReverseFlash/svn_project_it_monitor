package com.envisioniot.uscada.monitor.web.controller;

import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.entity.DashBoard;
import com.envisioniot.uscada.monitor.web.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * DashBoardController
 *
 * @author yangkang
 * @date 2021/3/3
 */
@RestController
@RequestMapping("/dashBoard")
public class DashBoardController {

    @Autowired
    private DashBoardService dashBoardService;

    @GetMapping("/init")
    public Response dashBoard() {
        DashBoard dashBoard = dashBoardService.init();
        return new Response(ResponseCode.SUCCESS.getCode(), dashBoard);
    }

    @GetMapping("/getAllAlarm")
    public Response getHostAlarm() {
        return new Response(ResponseCode.SUCCESS.getCode(), dashBoardService.getAllAlarm());
    }
}
