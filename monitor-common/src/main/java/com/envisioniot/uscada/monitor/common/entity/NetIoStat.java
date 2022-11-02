package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 */
@Data
@ToString
public class NetIoStat implements Serializable {

    private static final long serialVersionUID = -1293669692603358953L;

    /**
     * 接收的总数据包,rxpck
     */
    protected Long rxpck;

    /**
     * 发送的总数据包,txpck
     */
    protected Long txpck;

    /**
     * 接收的总KB数,rxkB
     */
    protected Long rxbyt;


    /**
     * 发送的总KB数,txkB
     */
    protected Long txbyt;

    /**
     * 接收到的错误包数
     */
    protected Long rxErrors;

    /**
     * 发送数据包时的错误数
     */
    protected Long txErrors;

    /**
     * 接收时丢弃的包数
     */
    protected Long rxDrops;

    /**
     * 发送时丢弃的包数
     */
    protected Long txDrops;

    /**
     * 总带宽kbps
     */
    private Double totalBandWidth;

    /**
     * 网络流量kbps
     */
    private Double totalNetFlow;

    /**
     * 网络输入流量kbps
     */
    private Double totalNetIn;

    /**
     * 网络输出流量kbps
     */
    private Double totalNetOut;

    /**
     * 网卡名称
     */
    private String ifName;
}