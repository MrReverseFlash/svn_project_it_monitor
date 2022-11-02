package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-18
 */
@Data
public class ContainerStat implements Serializable {
    private static final long serialVersionUID = 2915743908344310577L;

    /**
     * 容器flag
     */
    private String flag;
    private String occurTime;
    private String image;
    private String id;
    private String name;

    /**
     * cpu使用率%
     */
    private Double cpuPer;
    /**
     * 内存使用率%
     */
    private Double memPer;

    private Integer status;
    private Long pids;
    /**
     * 网络接口发送数据KB
     */
    private Long netIoSend;
    /**
     * 网络接口接受数据量KB
     */
    private Long netIoReceive;
    /**
     * 块设备读取数据量KB
     */
    private Long blockIoRead;
    /**
     * 块设备写入的数据量KB
     */
    private Long blockIoWrite;
    private String upTime;
    private String containerCreateTime;

    private String msg;
}
