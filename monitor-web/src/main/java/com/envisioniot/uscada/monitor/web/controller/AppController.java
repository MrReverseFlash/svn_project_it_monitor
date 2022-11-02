package com.envisioniot.uscada.monitor.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.common.entity.RunAppResp;
import com.envisioniot.uscada.monitor.web.entity.*;
import com.envisioniot.uscada.monitor.web.exception.WebRequestException;
import com.envisioniot.uscada.monitor.web.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Slf4j
@RestController
@RequestMapping(value = "/app")
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping("/getList")
    public Response getList(@RequestParam(name = "hostIp", required = false) String hostIp) {
        try {
            List<AppTable> apps;
            if (StrUtil.isBlank(hostIp)) {
                apps = appService.getAllMonitorApp();
            } else {
                apps = appService.getAppByIp(hostIp);
            }
            return new Response(ResponseCode.SUCCESS.getCode(), apps);
        } catch (Exception e) {
            throw new WebRequestException(e.getMessage());
        }
    }

    @GetMapping("/downloadExcel")
    public Response download(HttpServletResponse response, @RequestParam(name = "hostIp", required = false) String hostIp) {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        try {
            List<AppTable> apps;
            if (StrUtil.isBlank(hostIp)) {
                apps = appService.getAllMonitorApp();
            } else {
                apps = appService.getAppByIp(hostIp);
            }
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("进程信息" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), AppTable.class).sheet("sheet1").doWrite(apps);
        } catch (Exception e) {
            log.error("Excel 文件导出失败 : {}", e.getMessage());
            return new Response(ResponseCode.FAIL.getCode(), "Excel 文件导出失败 ");
        }
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/delete")
    public Response delete(@RequestBody DeleteAppRequest request) {
        appService.deleteApp(request);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/modifyName")
    public Response modifyName(@RequestParam("id") Integer id,
                               @RequestParam("name") String name) {
        appService.modifyName(id, name);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/getSample")
    public Response getSample(@RequestBody AppSampleRequest request) {
        List<AppSampleResp> sample = appService.getSample(request);
        return new Response(ResponseCode.SUCCESS.getCode(), sample);
    }

    @PostMapping("/add")
    public Response add(@RequestBody AppAddRequest request) {
        appService.addApp(request);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/getRun")
    public Response getRun(@RequestBody RunAppRequest request) {
        List<RunAppResp> runApp = appService.getRun(request);
        return new Response(ResponseCode.SUCCESS.getCode(), runApp);
    }
}
