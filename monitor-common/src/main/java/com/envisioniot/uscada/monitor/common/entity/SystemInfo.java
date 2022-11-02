package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 */
@Data
@ToString
public class SystemInfo implements Serializable {

    private static final long serialVersionUID = 4144072249600251841L;

    /**
     * 系统版本信息
     */
    private String version;

    /**
     * 系统版本详细信息
     */
    private String detail;


    /**
     * core的个数(即核数)
     */
    private Integer cpuCoreNum;


    /**
     * CPU型号信息
     */
    private String cpuVersion;

    /**
     * 进程总数
     */
    private Long processNum;

    /**
     * 僵尸进程数量
     */
    private Long zombieNum;

}