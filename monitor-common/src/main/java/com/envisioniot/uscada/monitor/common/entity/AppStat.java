package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hao.luo
 */
@Data
public class AppStat implements Serializable {

    private static final long serialVersionUID = 8209750064736948877L;

    /**
     * 应用进程ID
     */
    private Long appPid;

    /**
     * 应用进程唯一性标识符名称
     */
    private String appUid;

    /**
     * app的name
     */
    private String appName;

    /**
     * 内存使用M
     */
    private Double memUse;

    /**
     * 内存使用率%
     */
    private Double memPer;

    /**
     * cpu使用率
     */
    private Double cpuPer;

    /**
     * 进程读磁盘IO的字节数
     */
    private Long diskIoRead;

    /**
     * 进程写磁盘IO的字节数
     */
    private Long diskIoWritten;


    /**
     * 进程状态，1正常，2下线
     */
    private Integer status;

    /**
     * 进程启动时间
     */
    private String startTime;

    /**
     * 采样时间
     */
    private String occurTime;

    private Long threadNum;

    private String msg;

    /**
     * 进程所属用户
     */
    private String user;

}