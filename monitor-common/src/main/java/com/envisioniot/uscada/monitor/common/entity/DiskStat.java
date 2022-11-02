package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hao.luo
 */
@Data
public class DiskStat implements Serializable {

    private static final long serialVersionUID = -6151090325579776174L;

    private Boolean overlay = false;

    /**
     * 盘符类型
     */
    private String fileSystem;

    /**
     * 挂载磁盘符名称
     */
    private String dirName;

    /**
     * 分区大小GB
     */
    private Double size;

    /**
     * 已使用GB
     */
    private Double used;

    /**
     * 可用GB
     */
    private Double avail;

    /**
     * 已使用百分比%
     */
    private Double usePer;

    /**
     * inodes使用率百分比%
     */
    private Double inodesUsePer;

    private Long totalNodes;

    private Long useNodes;

    /**
     * 读总次数
     */
    private Long readCount;

    /**
     * 写总次数
     */
    private Long writeCount;

    /**
     * 读的总字节数
     */
    private Long readBytes;

    /**
     * 写的总字节数
     */
    private Long writeBytes;

    /**
     * 读的速率KB/s
     */
    private Double readRate;

    /**
     * 写的速率KB/s
     */
    private Double writeRate;
}