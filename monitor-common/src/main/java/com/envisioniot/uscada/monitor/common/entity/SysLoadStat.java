package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 */
@Data
@ToString
public class SysLoadStat implements Serializable {

    private static final long serialVersionUID = -5972558222472131223L;

    /**
     * 1分钟之前到现在的负载
     */
    private Double oneLoad;

    /**
     * 5分钟之前到现在的负载
     */
    private Double fiveLoad;

    /**
     * 15分钟之前到现在的负载
     */
    private Double fifteenLoad;
}