package com.envisioniot.uscada.monitor.web.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
@ToString
@ExcelIgnoreUnannotated
public class AppTable {
    private Integer id;
    @ExcelProperty(value = "主机ip",index = 0)
    private String ip;
    private Long appPid;
    @ExcelProperty(value = "进程路径",index = 2)
    private String appUid;
    @ExcelProperty(value = "进程名称",index = 1)
    private String appName;
    @ExcelProperty(value = "内存使用(MB)",index = 4)
    private Double memUse;
    private Double memPer;
    @ExcelProperty(value = "CPU使用率(%)",index = 3)
    private Double cpuPer;
    private Long diskIoRead;
    private Long diskIoWritten;
    private Integer threadNum;
    private String containerName;

    /**
     * 进程状态，1：正常，2：下线，空或null：待刷新
     */
    @ExcelProperty(value = "进程状态:1正常,2下线",index = 5)
    private Integer status;

    /**
     * 进程启动时间
     */
    private String startTime;

    /**
     * 采样时间
     */
    private String occurTime;
    @ExcelProperty(value = "更新时间",index = 7)
    private String updateTime;
    private String createTime;
    private String msg;

    /**
     * 主机状态，1：正常，2：下线
     */
    @ExcelProperty(value = "主机状态:1正常,2下线",index = 6)
    private Integer hostStatus;

    /**
     * 是否属于本区域监控主机
     */
    private Boolean hostMatch;
    private String matchFlag;
}
