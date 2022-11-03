package com.envisioniot.uscada.monitor.common.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Data
@ToString
@ExcelIgnoreUnannotated
public class HostComm {

    /**
     * host ip
     */
    @ExcelProperty(value = "主机ip",index = 0)
    protected String hostIp;

    /**
     * ssh端口
     */
    protected String port;

    /**
     * 主机名称
     */
    protected String hostName;


    /**
     * 记录采集时间
     */
    protected String occurTime;


    /**
     * 系统启动时间
     */
    protected String startTime;

    /**
     * 系统运行时间，单位秒
     */
    protected Integer runTime;

    /**
     * 系统当前登录用户数量
     */
    protected Integer logUserNum;

    /**
     * 主机状态，1表示正常、2表示中断、3表示删除（不监控）
     */
    @ExcelProperty(value ="主机状态:1正常,2中断,3删除",index = 1)
    protected Integer status;

    /**
     * agent与server连接方式，1表示：http，2：表示穿网闸文件方式
     */
    protected Integer type = 1;

    /**
     * 符合监控的主机
     */
    protected String matchFlag;


    /**
     * 是否属于本区域监控主机
     */
    private Boolean hostMatch;
}
