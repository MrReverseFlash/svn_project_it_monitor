package com.envisioniot.uscada.monitor.agent.controller;

import com.envisioniot.uscada.monitor.agent.component.SigarComponent;
import com.envisioniot.uscada.monitor.agent.service.monitor.impl.AppServiceImpl;
import com.envisioniot.uscada.monitor.agent.service.monitor.impl.DbServiceImpl;
import com.envisioniot.uscada.monitor.common.entity.DbInfo;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.common.entity.RunAppResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@RestController
public class CommController {

    @Autowired
    private AppServiceImpl appService;

    @Autowired
    private DbServiceImpl dbServiceImpl;

    @Autowired
    private SigarComponent sigarComponent;

    @GetMapping("getRun")
    public Response getRunApp(@RequestParam("exp") String exp, @RequestParam(value = "containerName", required = false) String containerName, @RequestParam(value = "isDocker", defaultValue = "false", required = false) Boolean isDocker) {
        List<RunAppResp> matchApp = appService.getMatchApp(exp, containerName, isDocker);
        return new Response(ResponseCode.SUCCESS.getCode(), matchApp);
    }

    @PostMapping("testDbConn")
    public Response testDbConn(@RequestBody DbInfo dbInfo) {
        if (dbServiceImpl.testDbConn(dbInfo)) {
            return new Response(ResponseCode.SUCCESS.getCode());
        } else {
            return new Response(ResponseCode.FAIL.getCode());
        }
    }

    @GetMapping("getConn/{flags}")
    public Response getConn(@PathVariable("flags") int flags) {
        String connStat = sigarComponent.getConnStat(flags);
        return new Response(ResponseCode.SUCCESS.getCode(), connStat);
    }
}
