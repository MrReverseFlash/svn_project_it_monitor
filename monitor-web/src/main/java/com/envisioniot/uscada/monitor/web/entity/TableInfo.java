package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * TableInfo
 *
 * @author yangkang
 * @date 2021/1/20
 */
@Data
public class TableInfo {
    private Long id;
    private String tableName;
    private String description;
    private String execSql;
    private Long recordNum;
    private String createTime;
    private String updateTime;
    private DbInfo dbInfo;
}
