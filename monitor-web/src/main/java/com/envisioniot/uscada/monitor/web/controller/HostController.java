package com.envisioniot.uscada.monitor.web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.envisioniot.uscada.monitor.common.entity.Response;
import com.envisioniot.uscada.monitor.common.entity.ResponseCode;
import com.envisioniot.uscada.monitor.web.entity.*;
import com.envisioniot.uscada.monitor.web.service.HostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.FILE_TIME_FORMAT;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Slf4j
@RestController
@RequestMapping(value = "/host")
public class HostController {

    @Autowired
    private HostService hostService;

    @GetMapping("/getPropList")
    public Response getPropList() {
        List<HostPropResp> hostPropResp = hostService.getPropList();
        return new Response(ResponseCode.SUCCESS.getCode(), hostPropResp);
    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link HostTable}
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. finish的时候会自动关闭OutputStream
     */
    @GetMapping("/downloadExcel")
    public Response download(HttpServletResponse response) {
        response.setContentType("application/force-download");
        response.setCharacterEncoding("utf-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("主机信息" + new SimpleDateFormat(FILE_TIME_FORMAT).format(new Date()), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "content-disposition");
            EasyExcel.write(response.getOutputStream(), HostTable.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("sheet1").doWrite(hostService.getTimeSeriesList());
        } catch (Exception e) {
            log.error("Excel 文件导出失败 : {}", e.getMessage());
            return new Response(ResponseCode.FAIL.getCode(), "Excel 文件导出失败 ");
        }
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/getList")
    public Response getList() {
        List<HostTable> timeSeriesList = hostService.getTimeSeriesList();
        return new Response(ResponseCode.SUCCESS.getCode(), timeSeriesList);
    }

    @PostMapping("/modifyLabel")
    public Response modifyLabel(@RequestBody LabelRequest request) {
        hostService.modifyLabel(request);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @PostMapping("/delHost")
    public Response delHost(@RequestBody LabelRequest request) {
        hostService.delHost(request);
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/getInfo")
    public Response getInfo(@RequestParam("hostIp") String hostIp) {
        HostTable hostTable = hostService.getInfo(hostIp);
        return new Response(ResponseCode.SUCCESS.getCode(), hostTable);
    }

    @GetMapping("/getSample")
    public Response getSample(@RequestParam("st") String st,
                              @RequestParam("et") String et,
                              @RequestParam("hostIp") String hostIp) {
        List<HostSampleResponse> sample = hostService.getSample(st, et, hostIp);
        return new Response(ResponseCode.SUCCESS.getCode(), sample);
    }

    @GetMapping("/getDetails")
    public Response getDetails(@RequestParam("st") String st,
                               @RequestParam("et") String et,
                               @RequestParam("hostIp") String hostIp) {
        List<HostDetailResponse> details = hostService.getDetails(st, et, hostIp);
        return new Response(ResponseCode.SUCCESS.getCode(), details);
    }

    @GetMapping("/downloadDetailsExcel")
    public Response downloadDetails(HttpServletResponse response,
                                    @RequestParam("st") String st,
                                    @RequestParam("et") String et,
                                    @RequestParam("hostIp") String hostIp) {
        response.setContentType("application/force-download");
        response.setCharacterEncoding("utf-8");
        try {
            List<HostDetailForDownload> details = hostService.getDetailsForDownload(st, et, hostIp);
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(hostIp + "_" + new SimpleDateFormat(FILE_TIME_FORMAT).format(new Date()), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "content-disposition");
            EasyExcel.write(response.getOutputStream(), HostDetailForDownload.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("sheet1").doWrite(details);
        } catch (Exception e) {
            log.error("Excel 文件导出失败 : {}", e.getMessage());
            return new Response(ResponseCode.FAIL.getCode(), "Excel 文件导出失败 ");
        }
        return new Response(ResponseCode.SUCCESS.getCode());
    }

    @GetMapping("/getNetSample")
    public Response getNetSample(@RequestParam("st") String st,
                                 @RequestParam("et") String et,
                                 @RequestParam("hostIp") String hostIp) {
        Map<String, List<NetSampleResponse>> netSample = hostService.getNetSample(st, et, hostIp);
        return new Response(ResponseCode.SUCCESS.getCode(), netSample);
    }

    @GetMapping("/getDiskSample")
    public Response getDiskSample(@RequestParam("st") String st,
                                  @RequestParam("et") String et,
                                  @RequestParam("hostIp") String hostIp) {
        Map<String, List<DiskSampleResponse>> netSample = hostService.getDiskSample(st, et, hostIp);
        return new Response(ResponseCode.SUCCESS.getCode(), netSample);
    }

    @GetMapping("/getHostAlarm")
    public Response getHostAlarm(@RequestParam("st") String st,
                                 @RequestParam("et") String et,
                                 @RequestParam("hostIp") String hostIp) {
        return new Response(ResponseCode.SUCCESS.getCode(), hostService.getHostAlarm(st, et, hostIp));
    }
}
