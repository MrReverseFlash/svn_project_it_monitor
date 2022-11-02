package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YK
 */
@Data
public class GatewayInfo implements Serializable {

    private static final long serialVersionUID = -3708253578162201158L;

    private String gatewayIp;

    private Short status;

    private Short operationMillis;
}
