package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2020-12-29
 */
@Data
@ToString
public class PortInfo {
    protected String mark;
    protected String portType;
    protected String portNum;
}
