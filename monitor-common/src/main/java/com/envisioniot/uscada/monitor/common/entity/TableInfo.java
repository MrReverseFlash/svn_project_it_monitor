package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
@Data
@ToString
public class TableInfo {
    private String tableName;
    private String sql;
    private Long num;
}
