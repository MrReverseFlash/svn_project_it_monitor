package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-17
 */
@Data
public class WhoStat implements Serializable {
    private static final long serialVersionUID = -2514509163796418610L;
    private String user;
    private String device;
    private String host;
    /**
     * 用户登录时间
     */
    private String logInTime;
}
