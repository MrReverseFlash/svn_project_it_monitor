package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-04
 */
@Data
@ToString
public class SwapStat {
    /**
     * 交换区使用率%
     */
    private Double swapPer;

    /**
     * 交换区总量MB
     */
    private Double swapTotal;

    /**
     * 交换区使用MB
     */
    private Double swapUsed;

    /**
     * B
     */
    private Long swapPageIn;

    /**
     * B
     */
    private Long swapPageOut;
}
