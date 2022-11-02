package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Data
@ToString
public class DiskTotalInfo {
    /**
     * 磁盘使用率%
     */
    private Double diskPer;

    /**
     * GB
     */
    private Double diskTotal;

    /**
     * GB
     */
    private Double diskUse;

    /**
     * inodes使用率
     */
    private Double inodesPer;

}
