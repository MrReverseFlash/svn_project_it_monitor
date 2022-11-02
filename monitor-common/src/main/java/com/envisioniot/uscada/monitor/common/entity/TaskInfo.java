package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
@Data
@ToString
public class TaskInfo implements Serializable {
    private static final long serialVersionUID = -210378724435508565L;
    private String cron;
    private boolean run;
    private String name;
    private Integer uuid;
}
