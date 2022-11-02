package com.envisioniot.uscada.monitor.common.entity;

import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-16
 */
@Data
@NoArgsConstructor
@ToString
public class PortStatSample extends PortInfo implements Serializable {
    private static final long serialVersionUID = 4383956783815612648L;
    private Integer status = CommConstants.OFFLINE_STATUS;
    private Integer listeningNum;
    private Integer establishedNum;
    private Integer timeWaitNum;
    private Integer closeWaitNum;
    private Integer synSentNum;
    private Integer idleNum;
    private String msg;
    private String occurTime;

    public PortStatSample(PortNetStat portNetStat) {
        this.listeningNum = portNetStat.getTcpListen();
        this.establishedNum = portNetStat.getTcpEstablished();
        this.timeWaitNum = portNetStat.getTcpTimeWait();
        this.closeWaitNum = portNetStat.getTcpCloseWait();
        this.synSentNum = portNetStat.getTcpSynSent();
        this.idleNum = portNetStat.getTcpIdle();
        this.status = portNetStat.getFinalStatus();
        this.portType = portNetStat.getPortTypeString();
    }
}
