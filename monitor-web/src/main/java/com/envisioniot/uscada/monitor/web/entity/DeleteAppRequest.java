package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
@ToString
public class DeleteAppRequest {
    private Integer id;
    private String appUid;
    private String hostIp;
}
