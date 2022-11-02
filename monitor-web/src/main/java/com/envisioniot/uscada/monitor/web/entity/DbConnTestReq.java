package com.envisioniot.uscada.monitor.web.entity;

import com.envisioniot.uscada.monitor.common.entity.DbInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class DbConnTestReq extends DbInfo {
    private String hostIp;
    private String port;
}
