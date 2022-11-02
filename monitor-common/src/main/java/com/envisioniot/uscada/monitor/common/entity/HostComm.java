package com.envisioniot.uscada.monitor.common.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Data
@ToString
public class HostComm {

    /**
     * host ip
     */
    @ExcelProperty("主机ip")
    protected String hostIp;

    /**
     * ssh端口
     */
    @ExcelIgnore
    protected String port;

    /**
     * 主机名称
     */
    @ExcelProperty("主机名称")
    protected String hostName;


    /**
     * 记录采集时间
     */
    @ExcelIgnore
    protected String occurTime;


    /**
     * 系统启动时间
     */
    @ExcelIgnore
    protected String startTime;

    /**
     * 系统运行时间，单位秒
     */
    @ExcelIgnore
    protected Integer runTime;

    /**
     * 系统当前登录用户数量
     */
    @ExcelIgnore
    protected Integer logUserNum;

    /**
     * 主机状态，1表示正常、2表示中断、3表示删除（不监控）
     */
    @ExcelProperty("主机状态，1正常、2中断、3删除")
    protected Integer status;

    /**
     * agent与server连接方式，1表示：http，2：表示穿网闸文件方式
     */
    @ExcelIgnore
    protected Integer type = 1;

    /**
     * 符合监控的主机
     */
    @ExcelIgnore
    protected String matchFlag;


    /**
     * 是否属于本区域监控主机
     */
    @ExcelIgnore
    private Boolean hostMatch;
}
