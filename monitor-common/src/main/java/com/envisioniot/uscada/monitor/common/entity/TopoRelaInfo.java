package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YK
 */
@Data
public class TopoRelaInfo implements Serializable {

    private static final long serialVersionUID = -8941116863564751908L;

    private String hostIpTarget;

    private Short status;

    private Short operationMillis;
}
