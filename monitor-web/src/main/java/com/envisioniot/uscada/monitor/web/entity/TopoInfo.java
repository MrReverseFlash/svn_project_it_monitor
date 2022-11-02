package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

import java.util.List;

/**
 * TopoInfo
 *
 * @author yangkang
 * @date 2021/3/3
 */
@Data
public class TopoInfo {
    private String hostIp;
    private Short hasAgent;
    private Float x;
    private Float y;
    private String updateTime;
    private String createTime;
    private String hostName;
    private String label;
    private Short status;
    private String uscadaStatus;
    private Short healthStatus;
    private String topoLevelId;
    private Short topoLevelNum;
    private String topoLevelName;
    private String topoSetId;
    private Short topoSetNum;
    private String topoSetName;
    private String topoPhysicId;
    private Short topoPhysicNum;
    private String topoPhysicName;
    private List<GatewayInfo> gatewayList;
    private List<TopoRela> topoRelaList;
}
