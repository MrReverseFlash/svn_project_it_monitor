package com.envisioniot.uscada.monitor.web.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
@ToString
public class AppTable {
    @ExcelIgnore
    private Integer id;
    @ExcelProperty("主机ip")
    private String ip;
    @ExcelIgnore
    private Long appPid;
    @ExcelProperty("进程路径")
    private String appUid;
    @ExcelProperty("进程名称")
    private String appName;
    @ExcelProperty("内存使用")
    private Double memUse;
    @ExcelIgnore
    private Double memPer;
    @ExcelProperty("CPU使用率")
    private Double cpuPer;
    @ExcelIgnore
    private Long diskIoRead;
    @ExcelIgnore
    private Long diskIoWritten;
    @ExcelIgnore
    private Integer threadNum;
    @ExcelIgnore
    private String containerName;

    /**
     * 进程状态，1：正常，2：下线，空或null：待刷新
     */
    @ExcelProperty("进程状态，1：正常，2：下线")
    private Integer status;

    /**
     * 进程启动时间
     */
    @ExcelIgnore
    private String startTime;

    /**
     * 采样时间
     */
    @ExcelIgnore
    private String occurTime;
    @ExcelProperty("更新时间")
    private String updateTime;
    @ExcelIgnore
    private String createTime;
    @ExcelIgnore
    private String msg;

    /**
     * 主机状态，1：正常，2：下线
     */
    @ExcelProperty("主机状态，1：正常，2：下线")
    private Integer hostStatus;

    /**
     * 是否属于本区域监控主机
     */
    private Boolean hostMatch;

    private String matchFlag;
}
