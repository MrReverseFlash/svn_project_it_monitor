package com.envisioniot.uscada.monitor.web.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.envisioniot.uscada.monitor.common.entity.HostComm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HostTable extends HostComm {
    @ExcelIgnore
    private Integer id;
    @ExcelProperty("资产名称")
    private String label;
    @ExcelProperty("系统版本")
    private String sysVersion;
    @ExcelProperty("操作系统类型")
    private String sysDetail;
    @ExcelIgnore
    private String cpuType;
    @ExcelIgnore
    private Integer cpuCoreNum;
    @ExcelProperty("内存使用率")
    private Float memPer;
    @ExcelProperty("cpu使用率")
    private Float cpuPer;
    @ExcelIgnore
    private Float cpuTemperature;
    @ExcelIgnore
    private Short processNum;
    @ExcelIgnore
    private Short zombieNum;
    @ExcelIgnore
    private Float totalMem;
    @ExcelIgnore
    private Float swapPer;
    @ExcelIgnore
    private Float swapTotal;
    @ExcelIgnore
    private Float netMaxPer;
    @ExcelIgnore
    private Float totalBandWidth;
    @ExcelIgnore
    private Float totalNetFlow;
    @ExcelIgnore
    private Float netPer;
    @ExcelProperty("磁盘使用率")
    private Float diskPer;
    @ExcelIgnore
    private Float diskUse;
    @ExcelIgnore
    private Float diskTotal;
    @ExcelIgnore
    private Float inodesPer;
    @ExcelProperty("更新时间")
    private String updateTime;
    @ExcelIgnore
    private String createTime;
    @ExcelIgnore
    private String uscadaStatus;
}
