package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 */
@Data
@ToString
public class CpuStat implements Serializable {

    private static final long serialVersionUID = -5748294532257701593L;

    /**
     * 用户态的CPU使用率%
     */
    private Double user;

    /**
     * 内核态CPU使用率%
     */
    private Double system;

    /**
     * 当前空闲率%
     */
    private Double idle;

    /**
     * cpu当前等待率%
     */
    private Double iowait;

    /**
     * 进程数
     */
    private Integer processNum;

    /**
     * 硬中断时间（%） 废弃
     */
    private Double irq;

    /**
     * 软中断时间（%） 废弃
     */
    private Double soft;

}