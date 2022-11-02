package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class HostStat extends HostComm implements Serializable {

    private static final long serialVersionUID = 7141487683724383094L;
    private SystemInfo systemInfo;
    private CpuStat cpuSample;
    private MemStat memSample;
    private SysLoadStat sysLoadSample;
    private List<NetIoStat> netSample;
    private List<DiskStat> diskList;
    private List<WhoStat> whoList;
    private List<UscadaStat> uscadaStatList;

    /**
     * 交换区信息
     */
    private SwapStat swapSample;

    /**
     * 磁盘统计信息
     */
    private DiskTotalInfo diskTotal;

    /**
     * 主机网卡统计信息
     */
    private NetTotalInfo netTotal;
}
