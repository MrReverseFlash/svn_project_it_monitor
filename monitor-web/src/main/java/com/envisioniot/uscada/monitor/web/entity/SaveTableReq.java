package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * SaveTableReq
 *
 * @author yangkang
 * @date 2021/1/20
 */
@Data
public class SaveTableReq {
    private Long id;
    private String tableName;
    private String description;
    private String execSql;
    private String dbId;
}
