package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class DbStat extends DbInfo implements Serializable {
    private static final long serialVersionUID = -7508071074311610384L;
    private String occurTime;
    /**
     * 数据库启动时间s
     */
    private Long upTime;
    /**
     * 数据库查询次数
     */
    private Long questions;
    /**
     * 数据库commit次数
     */
    private Long commitNum;
    /**
     * 数据库rollback次数
     */
    private Long rollbackNum;
    private Integer connMax;
    private Integer useCon;
    private Integer status;
    private List<TableInfo> tables;
    private String msg;
}
