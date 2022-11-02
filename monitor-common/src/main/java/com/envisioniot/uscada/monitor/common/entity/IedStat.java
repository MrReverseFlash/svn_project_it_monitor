package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IedStat extends IedInfo implements Serializable {
    private static final long serialVersionUID = -9062665007174300135L;
    private String occurTime;
    private Integer status;
    private String msg;
}
