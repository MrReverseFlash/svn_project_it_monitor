package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YK
 */
@Data
public class TopoInfo implements Serializable {

    private static final long serialVersionUID = 6129867588628988220L;

    private List<TopoRelaInfo> relaInfoList;

    private List<GatewayInfo> gatewayInfoList;

    private List<String> sameSetHostIpList;
}
