package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * DockerInfo
 *
 * @author yangkang
 * @date 2021/1/22
 */
@Data
public class DockerInfo {
    private Long id;
    private String hostIp;
    private String containerId;
    private String containerName;
    /**
     * 空：等待刷新；1：正常；2：断开；
     */
    private Integer status;
    private String uptime;
    private String containerCreateTime;
    private Double memPer;
    private Double cpuPer;
    private Long netIoSend;
    private Long netIoReceive;
    private Long blockIoRead;
    private Long blockIoWrite;
    private Integer pids;
    private String msg;
    private String image;
    private String createTime;
    private String updateTime;
    /**
     * 主机状态，1：正常，2：下线
     */
    private Integer hostStatus;
}
