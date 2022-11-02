package com.envisioniot.uscada.monitor.agent.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

import static com.envisioniot.uscada.monitor.agent.util.Constants.*;

/**
 * @author hao.luo
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "base")
public class CommonConfig implements InitializingBean {
    private String serverIp = "";
    private String serverPort = "";
    private String localIp = DEFAULT_LOCAL_IP;
    private String transporter = DEFAULT_TRANSPORTER;
    private boolean isHttp = Boolean.TRUE;
    private Integer retryNum = 3;
    private Long netInterval = 2000L;
    private Long diskInterval = 2000L;
    private Integer appUidLen = 200;
    private Boolean filterVirtualDisk = Boolean.TRUE;
    private Boolean filterOverlay = Boolean.TRUE;
    private String netCardName = "eth";
    private Set<Integer> serverCommType = new HashSet<>(4);
    private Set<Integer> clientCommType = new HashSet<>(4);
    private List<String> uscadaIpList = new LinkedList<>();

    @Override
    public void afterPropertiesSet() {
        getValidLocalIp();
    }

    private void getValidLocalIp() {
        if (!DEFAULT_LOCAL_IP.equalsIgnoreCase(this.localIp) && this.localIp != null) {
            return;
        }
        InetAddress localAddress0 = getLocalAddress0(this.netCardName);
        Assert.notNull(localAddress0, "get local ip failed.");
        this.localIp = localAddress0.getHostAddress();
        log.info("use local ip={}", this.localIp);
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress() || !address.isSiteLocalAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        return (name != null
                && !ANYHOST.equals(name)
                && !DEFAULT_LOCAL_IP.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }

    private static InetAddress getLocalAddress0(String netCardName) {
        InetAddress localAddress = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        if (network.isLoopback() || network.isVirtual() || !network.isUp()) {
                            log.warn("loopback={}, virtual={}, ip={}, ", network.isLoopback(), network.isVirtual(), network.isUp());
                            continue;
                        }
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        localAddress = address;
                                    }
                                } catch (Throwable e) {
                                    log.warn("Failed to retrieving ip address, " + e.getMessage(), e);
                                }
                            }
                        }
                        // linux 对于多虚机网卡情况
                        if (network.getName().startsWith(netCardName)) {
                            if (!StringUtils.isEmpty(localAddress))
                                return localAddress;
                        }
                    } catch (Throwable e) {
                        log.warn("Failed to retrieving ip address, " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            log.warn("Failed to retrieving ip address, " + e.getMessage(), e);
        }
        if (localAddress == null) {
            try {
                localAddress = InetAddress.getLocalHost();
                if (isValidAddress(localAddress)) {
                    return localAddress;
                }
            } catch (Throwable e) {
                log.warn("Failed to retriving ip address, " + e.getMessage(), e);
            }
        }
        return localAddress;
    }
}
