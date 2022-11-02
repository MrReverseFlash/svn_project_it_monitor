package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Data
@ToString
public class NetTotalInfo {
    /**
     * 总带宽kbps
     */
    private Double totalBandWidth;

    /**
     * 网络流量kbps
     */
    private Double totalNetFlow;

    /**
     * 网卡中最大网络使用率%
     */
    private Double netMaxPer;

    /**
     * 主机网络使用率%
     */
    private Double netPer;

}
