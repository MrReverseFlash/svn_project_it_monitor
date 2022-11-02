package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PeerInfo implements Serializable {
    private static final long serialVersionUID = -5215258689618423966L;
    protected String peerIp;
    protected String port;

    public PeerInfo(String remoteIp, String port) {
        this.peerIp = remoteIp;
        this.port = port;
    }
}
