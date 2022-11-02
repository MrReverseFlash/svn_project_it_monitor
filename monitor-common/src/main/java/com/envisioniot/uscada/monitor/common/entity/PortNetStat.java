package com.envisioniot.uscada.monitor.common.entity;

import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.Data;

/**
 * @author hao.luo
 * @date 2020-12-17
 */
@Data
public class PortNetStat {
    private Integer portType;

    private String portTypeString;

    private Integer status = CommConstants.OFFLINE_STATUS;

    protected int[] tcpStates = new int[14];

    public void setState(int state) {
        tcpStates[state] += 1;
    }

    public int getTcpEstablished() {
        return this.tcpStates[1];
    }

    public int getTcpSynSent() {
        return this.tcpStates[2];
    }

    public int getTcpSynRecv() {
        return this.tcpStates[3];
    }

    public int getTcpFinWait1() {
        return this.tcpStates[4];
    }

    public int getTcpFinWait2() {
        return this.tcpStates[5];
    }

    public int getTcpTimeWait() {
        return this.tcpStates[6];
    }

    public int getTcpClose() {
        return this.tcpStates[7];
    }

    public int getTcpCloseWait() {
        return this.tcpStates[8];
    }

    public int getTcpLastAck() {
        return this.tcpStates[9];
    }

    public int getTcpListen() {
        return this.tcpStates[10];
    }

    public int getTcpClosing() {
        return this.tcpStates[11];
    }

    public int getTcpIdle() {
        return this.tcpStates[12];
    }

    public int getTcpBound() {
        return this.tcpStates[13];
    }

    /**
     * @return 获取最后的统计状态
     */
    public int getFinalStatus() {
        if (getTcpListen() + getTcpEstablished() + getTcpSynSent() + getTcpIdle() > 0) {
            return CommConstants.ONLINE_STATUS;
        } else {
            return status;
        }
    }
}
