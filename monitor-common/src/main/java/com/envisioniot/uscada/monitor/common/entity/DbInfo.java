package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
@Data
@ToString
public class DbInfo {
    //改用UUID
    protected String id;
    protected String dbName;
    protected String dbType;
    protected String user;
    protected String passwd;
    protected String dbIp;
    protected String dbPort;
    protected String version;
}
