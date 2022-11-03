package com.envisioniot.uscada.monitor.web.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
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
@ExcelIgnoreUnannotated
public class HostTable extends HostComm {
    private Integer id;
    @ExcelProperty(value = "资产名称", index = 2)
    private String label;
    private String sysVersion;
    @ExcelProperty(value ="操作系统类型",index = 3)
    private String sysDetail;
    private String cpuType;
    private Integer cpuCoreNum;
    @ExcelProperty(value = "内存使用率(%)",index = 5)
    private Float memPer;
    @ExcelProperty(value = "cpu使用率(%)",index = 4)
    private Float cpuPer;
    private Float cpuTemperature;
    private Short processNum;
    private Short zombieNum;
    private Float totalMem;
    private Float swapPer;
    private Float swapTotal;
    private Float netMaxPer;
    private Float totalBandWidth;
    private Float totalNetFlow;
    private Float netPer;
    @ExcelProperty(value = "磁盘使用率(%)",index = 6)
    private Float diskPer;
    private Float diskUse;
    private Float diskTotal;
    private Float inodesPer;
    @ExcelProperty(value = "更新时间",index = 7)
    private String updateTime;
    private String createTime;
    private String uscadaStatus;
}
