package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class IedInfo extends PeerInfo implements Serializable {
    private static final long serialVersionUID = -2461954276207198538L;
    protected String iedName;
    protected String iedAlias;
    protected Integer commType;
}
