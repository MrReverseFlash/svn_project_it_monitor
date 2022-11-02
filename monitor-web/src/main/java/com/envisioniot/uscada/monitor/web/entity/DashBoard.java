package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

import java.util.List;

/**
 * DashBoard
 *
 * @author yangkang
 * @date 2021/3/3
 */
@Data
public class DashBoard {
    private Integer hostNum;
    private Integer hostAlarm;
    private Integer dbNum;
    private Integer dbAlarm;
    private Integer appNum;
    private Integer appAlarm;
    private Integer portNum;
    private Integer portAlarm;
    private Integer hostOnLineNum;
    private Integer hostOffLineNum;
    private Integer uscadaRunningNum;
    private Integer uscadaExitNum;
    private List<HostTable> cpuTmpTop10;
    private List<HostTable> cpuPerTop10;
    private List<HostTable> memPerTop10;
    private List<HostTable> diskPerTop10;
    private List<HostTable> netPerTop10;
}
