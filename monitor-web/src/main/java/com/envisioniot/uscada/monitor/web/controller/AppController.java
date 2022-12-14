package com.envisioniot.uscada.monitor.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.FILE_TIME_FORMAT;

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
        response.setContentType("application/force-download");
        response.setCharacterEncoding("utf-8");
        try {
            List<AppTable> apps;
            if (StrUtil.isBlank(hostIp)) {
                apps = appService.getAllMonitorApp();
            } else {
                apps = appService.getAppByIp(hostIp);
            }
            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
            String fileName = URLEncoder.encode("????????????" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            EasyExcel.write(response.getOutputStream(), AppTable.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("sheet1").doWrite(apps);
        } catch (Exception e) {
            log.error("Excel ?????????????????? : {}", e.getMessage());
            return new Response(ResponseCode.FAIL.getCode(), "Excel ?????????????????? ");
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

    @PostMapping("/downloadDetailsExcel")
    public Response downloadDetail(HttpServletResponse response, @RequestBody AppSampleRequest request) {
        response.setContentType("application/force-download");
        response.setCharacterEncoding("utf-8");
        try {
            List<AppSampleResp> sample = appService.getSample(request);
            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
            String fileName = URLEncoder.encode(request.getHostIp() + "_" + request.getAppName() + "_" + new SimpleDateFormat(FILE_TIME_FORMAT).format(new Date()), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            EasyExcel.write(response.getOutputStream(), AppSampleResp.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("sheet1").doWrite(sample);
        } catch (Exception e) {
            log.error("Excel ?????????????????? : {}", e.getMessage());
            return new Response(ResponseCode.FAIL.getCode(), "Excel ?????????????????? ");
        }
        return new Response(ResponseCode.SUCCESS.getCode());
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
