package com.envisioniot.uscada.monitor.agent.component;

import cn.hutool.core.util.NumberUtil;
import com.envisioniot.uscada.monitor.common.entity.NetIoStat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.envisioniot.uscada.monitor.agent.util.Constants.DEFAULT_PRECISION;

@Component
@Slf4j
public class OshiComponent {

    private static final SystemInfo si;
    private static final HardwareAbstractionLayer hal;

    static {
        si = new SystemInfo();
        hal = si.getHardware();
    }

    /**
     * @return NetIsState 获取机器的网络流量信息
     */
    public Map<String, NetIoStat> net() {
        Map<String, NetIoStat> netMaps = new HashMap<>(8);
        try {
            List<NetworkIF> networkIFs = hal.getNetworkIFs();
            for (NetworkIF net : networkIFs) {
                if (!net.isConnectorPresent()) {
                    log.warn("!IFF_UP...skipping getNetInterfaceStat, net interface name={}", net.getName());
                    continue;
                }
                NetIoStat netIoStat = new NetIoStat();
                // 接收到的总字节数 KB
                netIoStat.setRxbyt((net.getBytesRecv() / 1000));
                // 发送的总字节数 KB
                netIoStat.setTxbyt((net.getBytesSent() / 1000));
                // 接收的总包裹数
                netIoStat.setRxpck(net.getPacketsRecv());
                // 发送的总包裹数
                netIoStat.setTxpck(net.getPacketsSent());
                // 接收到的错误包数
                netIoStat.setRxErrors(net.getInErrors());
                // 发送数据包时的错误数
                netIoStat.setTxErrors(net.getOutErrors());
                // 接收时丢弃的包数
                netIoStat.setRxDrops(net.getInDrops());
                // 发送时丢弃的包数
                netIoStat.setTxDrops(net.getOutErrors());
                // 网卡带宽kbps
                netIoStat.setTotalBandWidth(NumberUtil.div(net.getSpeed(), 1000, DEFAULT_PRECISION));
                netIoStat.setIfName(net.getName());
                netMaps.put(net.getName(), netIoStat);
                log.info("net info : {}", netIoStat);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("get network information fail.");
        }
        return netMaps;
    }

    public static void main(String[] args) {
        new OshiComponent().net();
    }

}
