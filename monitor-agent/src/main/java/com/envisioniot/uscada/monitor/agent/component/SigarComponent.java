package com.envisioniot.uscada.monitor.agent.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.system.OsInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.envisioniot.uscada.monitor.agent.config.CommonConfig;
import com.envisioniot.uscada.monitor.agent.util.FormatUtil;
import com.envisioniot.uscada.monitor.agent.util.UscadaClient;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.*;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Uptime;
import org.hyperic.sigar.Who;
import org.hyperic.sigar.ptql.ProcessFinder;
import org.hyperic.sigar.ptql.ProcessQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.envisioniot.uscada.monitor.agent.util.Constants.*;

/**
 * @author hao.luo
 * @date 2020-12-15
 */
@Component
@Slf4j
public class SigarComponent {
    private static final Sigar SIGAR;
    private static final OperatingSystem OS;

    @Autowired
    private CommonConfig commonConfig;

    static {
        String libPath = ClassUtil.getClassPath() + File.separator + "lib";
        if (!FileUtil.exist(libPath)) {
            libPath = System.getProperty("user.dir") + File.separator + "lib";
            if (!FileUtil.exist(libPath)) {
                throw new IllegalArgumentException(String.format("Both path={%s} and {%s} not exist!", libPath, ClassUtil.getClassPath() + File.separator + "lib"));
            }
        }
        String lib = System.getProperty("java.library.path");
        OsInfo osInfo = new OsInfo();
        if (osInfo.isWindows()) {
            lib = libPath + ";" + lib;
        } else {
            lib = libPath + ":" + lib;
        }
        System.setProperty("java.library.path", lib);
        SIGAR = new Sigar();
        OS = OperatingSystem.getInstance();
    }

    /**
     * 获取内存使用信息
     *
     * @return MemStat 内存采样信息
     */
    public MemStat memory() {
        MemStat memStat = null;
        try {
            Mem mem = SIGAR.getMem();
            double total = NumberUtil.div((float) mem.getTotal(), MB, DEFAULT_PRECISION);
            double used = NumberUtil.div((float) mem.getActualUsed(), MB, DEFAULT_PRECISION);
            double free = NumberUtil.div((float) mem.getActualFree(), MB, DEFAULT_PRECISION);
            double usePer = NumberUtil.div(used * PERCENT, total, PERCENT_PRECISION);
            memStat = new MemStat();
            memStat.setUsePer(usePer);
            memStat.setFree(free);
            memStat.setUsed(used);
            memStat.setTotal(total);
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get host memory fail.");
        }
        return memStat;
    }

    /**
     * 获取cpu使用率，等待率，空闲率
     *
     * @return CpuStat cpu 的采样数据
     */
    public CpuStat cpu() {
        CpuStat cpuStat = null;
        try {
            CpuPerc[] cpus = SIGAR.getCpuPercList();
            double sys = 0.0, user = 0.0, wait = 0.0, idle = 0.0, irq = 0.0, soft = 0.0;
            for (CpuPerc cpu : cpus) {
                sys += cpu.getSys();
                user += cpu.getUser();
                wait += cpu.getWait();
                idle += cpu.getIdle();
                irq += cpu.getIrq();
                soft += cpu.getSoftIrq();
            }
            cpuStat = new CpuStat();
            cpuStat.setSystem(FormatUtil.formatDouble(sys * PERCENT / cpus.length, PERCENT_PRECISION));
            cpuStat.setUser(FormatUtil.formatDouble(user * PERCENT / cpus.length, PERCENT_PRECISION));
            cpuStat.setIowait(FormatUtil.formatDouble(wait * PERCENT / cpus.length, PERCENT_PRECISION));
            cpuStat.setIdle(FormatUtil.formatDouble(idle * PERCENT / cpus.length, PERCENT_PRECISION));
            cpuStat.setIrq(FormatUtil.formatDouble(irq * PERCENT / cpus.length, PERCENT_PRECISION));
            cpuStat.setSoft(FormatUtil.formatDouble(soft * PERCENT / cpus.length, PERCENT_PRECISION));
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get host cpu info fail.");
        }
        return cpuStat;
    }

    public ProcStat getState() {
        ProcStat procStat = null;
        try {
            procStat = SIGAR.getProcStat();
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get operate system stat information fail.");
        }
        return procStat;
    }

    /**
     * @return SystemInfo 获取操作系统信息
     */
    public SystemInfo os() {
        SystemInfo systemInfo = null;
        try {
            CpuInfo[] cpus = SIGAR.getCpuInfoList();
            systemInfo = new SystemInfo();
            systemInfo.setCpuCoreNum(cpus.length);
            if (cpus.length > 0) {
                // TODO 注意model中在linux 只是显示Xeon
                systemInfo.setCpuVersion(cpus[0].getModel() + " " + cpus[0].getVendor() + " " + cpus[0].getMhz());
            }
            systemInfo.setVersion(OS.getVersion());
            systemInfo.setDetail(OS.getDescription() + " " + OS.getArch() + " " + OS.getVendorVersion() + " " + OS.getVendorName() + " " + OS.getVendor() + " " + OS.getMachine());
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get operate system information fail.");
        }
        return systemInfo;
    }


    /**
     * @return List<WhoStat> 所有登录的用户
     */
    public List<WhoStat> who() {
        Sigar sigar = new Sigar();
        List<WhoStat> whoStateList = null;
        try {
            Who[] whoList = sigar.getWhoList();
            if (whoList != null && whoList.length > 0) {
                whoStateList = new ArrayList<>(4);
                for (Who who : whoList) {
                    WhoStat whoStat = new WhoStat();
                    whoStat.setDevice(who.getDevice());
                    whoStat.setHost(who.getHost());
                    whoStat.setLogInTime(FormatUtil.getStartTime(who.getTime() * 1000L));
                    whoStat.setUser(who.getUser());
                    whoStateList.add(whoStat);
                }
            }
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("fail to get log in user information.");
        }
        return whoStateList;
    }

    /**
     * 获取磁盘使用信息
     */
    public Map<String, DiskStat> diskInfo(boolean filterVirtualDisk) {
        Map<String, DiskStat> diskStatMap = new HashMap<>(4);
        FileSystem[] files;
        try {
            files = SIGAR.getFileSystemList();
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get all disk information fail.");
            return diskStatMap;
        }

        for (FileSystem fileSystem : files) {
            try {
                log.info("Each file system info : " + JSON.toJSONString(fileSystem));
                String dirName = fileSystem.getDirName();
                FileSystemUsage usage = SIGAR.getFileSystemUsage(dirName);
                String devName = fileSystem.getDevName();
                if ((filterVirtualDisk && usage.getTotal() <= 0) || fileSystem instanceof NfsFileSystem) {
                    continue;
                }
                DiskStat deskState = new DiskStat();
                deskState.setOverlay("overlay".equalsIgnoreCase(devName));
                deskState.setDirName(dirName);
                deskState.setFileSystem(devName);
                double result = NumberUtil.div((float) usage.getUsed(), MB, DEFAULT_PRECISION);
                deskState.setUsed(result);
                result = NumberUtil.div((float) usage.getAvail(), MB, DEFAULT_PRECISION);
                deskState.setAvail(result);
                result = NumberUtil.div((float) usage.getTotal(), MB, DEFAULT_PRECISION);
                deskState.setSize(result);
                double usePercent;
                if (usage.getTotal() != 0) {
                    usePercent = NumberUtil.div(usage.getUsed() * PERCENT, usage.getTotal(), PERCENT_PRECISION);
                } else {
                    usePercent = FormatUtil.formatDouble(usage.getUsePercent() * PERCENT, PERCENT_PRECISION);
                }
                deskState.setUsePer(usePercent);

                // inodes使用率
                long usedInodes = usage.getFiles() - usage.getFreeFiles();
                long totalInodes = usage.getFiles();
                double pct = 0.0;
                if (totalInodes != 0L) {
                    pct = NumberUtil.div(usedInodes * PERCENT, totalInodes, DEFAULT_PRECISION);
                }
                deskState.setUseNodes(usedInodes);
                deskState.setTotalNodes(totalInodes);
                deskState.setInodesUsePer(pct);
                deskState.setReadCount(usage.getDiskReads());
                deskState.setWriteCount(usage.getDiskWrites());
                deskState.setReadBytes(usage.getDiskReadBytes());
                deskState.setWriteBytes(usage.getDiskWriteBytes());
                diskStatMap.put(dirName, deskState);
            } catch (SigarException e) {
                log.error(e.toString(), e);
                log.error("get disk={} information fail.", fileSystem.getDevName());
            }
        }
        return diskStatMap;
    }

    /**
     * @return SysLoadStat 获取系统负载
     */
    public SysLoadStat getSysLoad() {
        String osName = System.getProperty("os.name");
        // windows 机器不支持负载
        if (osName.toLowerCase().startsWith(CommConstants.WIN_FLAG)) {
            log.error(" sigar not support get system load information in windows platform.");
            return null;
        }
        SysLoadStat sysLoadStat = null;
        try {
            double[] load = SIGAR.getLoadAverage();
            sysLoadStat = new SysLoadStat();
            sysLoadStat.setOneLoad(FormatUtil.formatDouble(load[0], DEFAULT_PRECISION));
            sysLoadStat.setFiveLoad(FormatUtil.formatDouble(load[1], DEFAULT_PRECISION));
            sysLoadStat.setFifteenLoad(FormatUtil.formatDouble(load[2], DEFAULT_PRECISION));
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get system load information fail.");
        }
        return sysLoadStat;
    }

    /**
     * @param pid       进程号
     * @param exp       进程匹配表达式
     * @param appUidLen app uid最大长度
     * @return app uid
     * @throws SigarException
     */
    private String getAppUid(long pid, String exp, Integer appUidLen) throws SigarException {
        String[] procArgs = SIGAR.getProcArgs(pid);
        if (procArgs == null) {
            return null;
        }
        StringBuilder appUid = new StringBuilder();
        for (int i = procArgs.length - 1; i >= 0; i--) {
            String procArg = procArgs[i];
            String subStr = procArg.substring(Math.max(0, procArg.length() - appUidLen));
            if (i == procArgs.length - 1) {
                appUid.append(subStr);
            } else {
                appUid.insert(0, subStr + APP_DELIMIT);
            }
            if (appUid.length() >= appUidLen) {
                return appUid.toString();
            }
        }
        return appUid.toString();
//        String lowerExp = exp.toLowerCase();
//        for (String procArg : procArgs) {
//            String arg = procArg.toLowerCase();
//            int begin = arg.indexOf(lowerExp);
//            if (begin >= 0) {
//                int end = Math.min(arg.length(), begin + appUidLen);
//                return procArg.substring(begin, end);
//            }
//        }
//        return null;
    }

    /**
     * @param exp           正则表达式
     * @param appUidLen     appUid最大长度
     * @param containerPids 容器中pids
     * @param include       是否是容器中的pid, true表示是容器的pid，false表示不是
     * @return 所有匹配的pids
     */
    public List<RunAppResp> getMatchApp(String exp, Integer appUidLen, Set<Long> containerPids, Boolean include) throws SigarException {
        List<RunAppResp> matchApp = new ArrayList<>();
        String escape = ReUtil.escape(exp);
        String query = "Args.*.re=(?i)" + escape;
        long[] pids;
        synchronized (this) {
            try {
                pids = ProcessFinder.find(SIGAR, query);
            } finally {
                ProcessQueryFactory.getInstance().clear();
            }
        }
        if (pids == null) {
            return matchApp;
        }
        if (include) {
            pids = Arrays.stream(pids).filter(containerPids::contains).toArray();
        } else {
            pids = Arrays.stream(pids).filter(pid -> !containerPids.contains(pid)).toArray();
        }
        if (pids.length == 1) {
            matchApp.add(new RunAppResp(pids[0], exp));
            return matchApp;
        }
        for (long pid : pids) {
            String appUid = getAppUid(pid, exp, appUidLen);
            if (StringUtils.isEmpty(appUid)) {
                log.error("pid={} has no args.", pid);
                continue;
            }
            matchApp.add(new RunAppResp(pid, appUid));
        }
        return matchApp;
    }

    private Long getAppPid(String appUid, Set<Long> containerPids, Boolean include) throws SigarException {
        String[] args = appUid.split(APP_DELIMIT);
        Set<Long> matchPid = new HashSet<>();
        for (String arg : args) {
            String escape = ReUtil.escape(arg);
            String query = "Args.*.re=(?i)" + escape;
            long[] pids = ProcessFinder.find(SIGAR, query);
            if (pids == null) {
                String msg = "app uid = {" + appUid + "} cmd = {" + query + "} is invalid, pls check";
                log.error(msg);
                return null;
            }
            if (CollectionUtil.isEmpty(matchPid)) {
                for (long pid : pids) {
                    matchPid.add(pid);
                }
            } else {
                Set<Long> items = new HashSet<>();
                for (long pid : pids) {
                    if (matchPid.contains(pid)) {
                        items.add(pid);
                    }
                }
                matchPid.clear();
                matchPid.addAll(items);
            }
            if (CollectionUtil.isEmpty(matchPid)) {
                String msg = "app uid = {" + appUid + "} cmd = {" + query + "} is invalid, pls check";
                log.error(msg);
                return null;
            }
        }
        List<Long> result;
        if (include) {
            result = matchPid.stream().filter(containerPids::contains).collect(Collectors.toList());
        } else {
            result = matchPid.stream().filter(pid -> !containerPids.contains(pid)).collect(Collectors.toList());
        }
        if (result.size() != 1) {
            String msg = "app uid = {" + appUid + "} is not unique, pls check";
            log.error(msg);
            log.error("match pids={}", matchPid);
            return null;
        }
        return result.get(0);
    }

    /**
     * @param appUid        进程唯一标识符
     * @param containerPids 容器内部进程id
     * @param include       是否是获取容器内部进度id
     * @return 获取进程信息
     */
    public AppStat getLoadPidByAppUid(String appUid, Set<Long> containerPids, Boolean include) {
        AppStat appStat = new AppStat();
        appStat.setAppUid(appUid);
        appStat.setOccurTime(DateUtil.now());
        appStat.setStatus(CommConstants.OFFLINE_STATUS);
        synchronized (this) {
            try {
                Long pid = getAppPid(appUid, containerPids, include);
                if (pid == null) {
                    String msg = "app uid = {" + appUid + "} is invalid, pls check";
                    log.error(msg);
                    appStat.setMsg(msg);
                    return appStat;
                }
                appStat.setStatus(CommConstants.ONLINE_STATUS);
                appStat.setAppPid(pid);
                ProcCpu procCpu = SIGAR.getProcCpu(pid);
                appStat.setCpuPer(FormatUtil.formatDouble(procCpu.getPercent(), PERCENT_PRECISION));
                ProcMem procMem = SIGAR.getProcMem(pid);
                appStat.setMemUse(NumberUtil.div((float) procMem.getResident(), MB, DEFAULT_PRECISION));
                ProcState procState = SIGAR.getProcState(pid);
                appStat.setThreadNum(procState.getThreads());
                appStat.setAppName(procState.getName());
                ProcCredName cred = SIGAR.getProcCredName(pid);
                appStat.setUser(cred.getUser());
                appStat.setStartTime(FormatUtil.getStartTime(procCpu.getStartTime()));
                // TODO ,  app的IO获取没有读取。
            } catch (SigarException e) {
                log.error(e.getMessage(), e);
                String msg = "app uid = {" + appUid + "} is invalid, pls check";
                log.error(msg);
                appStat.setMsg(msg);
            } finally {
                ProcessQueryFactory.getInstance().clear();
            }
        }
        return appStat;
    }

    /**
     * @return NetIsState 获取机器的网络流量信息
     */
    public Map<String, NetIoStat> net() {
        Map<String, NetIoStat> netMaps = new HashMap<>(8);
        try {
            String[] ifNames = SIGAR.getNetInterfaceList();
            for (String name : ifNames) {
                NetInterfaceConfig ifconfig = SIGAR.getNetInterfaceConfig(name);
                if ((ifconfig.getFlags() & 1L) <= 0L) {
                    log.warn("!IFF_UP...skipping getNetInterfaceStat, net interface name={}", name);
                    continue;
                }
                NetInterfaceStat ifstat = SIGAR.getNetInterfaceStat(name);
                NetIoStat netIoStat = new NetIoStat();
                // 接收到的总字节数 KB
                netIoStat.setRxbyt((ifstat.getRxBytes() / 1000));
                // 发送的总字节数 KB
                netIoStat.setTxbyt((ifstat.getTxBytes() / 1000));
                // 接收的总包裹数
                netIoStat.setRxpck(ifstat.getRxPackets());
                // 发送的总包裹数
                netIoStat.setTxpck(ifstat.getTxPackets());
                // 接收到的错误包数
                netIoStat.setRxErrors(ifstat.getRxErrors());
                // 发送数据包时的错误数
                netIoStat.setTxErrors(ifstat.getTxErrors());
                // 接收时丢弃的包数
                netIoStat.setRxDrops(ifstat.getRxDropped());
                // 发送时丢弃的包数
                netIoStat.setTxDrops(ifstat.getTxDropped());
                // 网卡带宽kbps
                netIoStat.setTotalBandWidth(NumberUtil.div(ifstat.getSpeed(), 1000, DEFAULT_PRECISION));
                netIoStat.setIfName(name);
                netMaps.put(name, netIoStat);
            }
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get network information fail.");
        }
        return netMaps;
    }

    /**
     * @return 系统启动时间秒
     */
    public Integer getHostUptime() {
        try {
            Uptime uptime = SIGAR.getUptime();
            return (int) uptime.getUptime();
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get host uptime fail.");
        }
        return -1;
    }

    /**
     * @return 获取本地端口监听状态，支持TCP、UDP
     */
    public Map<Long, PortNetStat> getLocalPortNetStat() {
        Map<Long, PortNetStat> portMaps = new HashMap<>(4);
        try {
            NetConnection[] connections = SIGAR.getNetConnectionList(243);
            if (connections == null) {
                log.error("fail to get all port information.");
                return portMaps;
            }
            for (NetConnection conn : connections) {
                String localIp = conn.getLocalAddress();
//                if (NetFlags.isAnyAddress(localIp)) {
//                    continue;
//                }
                long localPort = conn.getLocalPort();
                PortNetStat portStat = portMaps.get(localPort);
                if (portStat == null) {
                    portStat = new PortNetStat();
                    portStat.setPortType(conn.getType());
                    portStat.setPortTypeString(conn.getTypeString().toLowerCase());
                    portMaps.put(localPort, portStat);
                }
                if (conn.getType() == UDP_TYPE) {
                    portStat.setStatus(CommConstants.ONLINE_STATUS);
                } else {
                    portStat.setState(conn.getState());
                }
            }
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get port information fail.");
        }
        return portMaps;
    }

    /**
     * @param conn  net connection
     * @param peers 监控对象
     * @return 获取需要监控对象
     */
    private PeerInfo getMonitorIed(NetConnection conn, Map<PeerInfo, Integer> peers) {
        Set<Integer> clientCommType = commonConfig.getClientCommType();
        Set<Integer> serverCommType = commonConfig.getServerCommType();
        String remoteIp = conn.getRemoteAddress();
        long remotePort = conn.getRemotePort();
        PeerInfo peer = new PeerInfo(remoteIp, String.valueOf(remotePort));
        Integer commType = peers.get(peer);
        if (commType != null && clientCommType.contains(commType)) {
            return peer;
        }
        long localPort = conn.getLocalPort();
        peer = new PeerInfo(remoteIp, String.valueOf(localPort));
        commType = peers.get(peer);
        if (commType != null && serverCommType.contains(commType)) {
            return peer;
        }
        return null;
    }

    /**
     * @param peers 对端机器的信息
     * @return 对端机器的通讯状态
     */
    public Map<PeerInfo, PortNetStat> getRemoteNetStat(Map<PeerInfo, Integer> peers) {
        Map<PeerInfo, PortNetStat> remoteMaps = new HashMap<>(4);
        try {
            NetConnection[] connections = SIGAR.getNetConnectionList(243);
            if (connections == null) {
                log.error("fail to get all port information.");
                return remoteMaps;
            }
            for (NetConnection conn : connections) {
                PeerInfo monitorIed = getMonitorIed(conn, peers);
                if (monitorIed == null) {
                    continue;
                }
                PortNetStat portStat = remoteMaps.get(monitorIed);
                if (portStat == null) {
                    portStat = new PortNetStat();
                    portStat.setPortType(conn.getType());
                    portStat.setPortTypeString(conn.getTypeString());
                    remoteMaps.put(monitorIed, portStat);
                }
                if (conn.getType() == UDP_TYPE) {
                    portStat.setStatus(CommConstants.ONLINE_STATUS);
                } else {
                    portStat.setState(conn.getState());
                }
            }
        } catch (SigarException e) {
            log.error(e.getMessage(), e);
            log.error("get port information fail.");
        }
        return remoteMaps;
    }

    public SwapStat swap() {
        SwapStat swapStat = null;
        try {
            Swap swap = SIGAR.getSwap();
            double total = NumberUtil.div((float) swap.getTotal(), MB, DEFAULT_PRECISION);
            double used = NumberUtil.div((float) swap.getUsed(), MB, DEFAULT_PRECISION);
            swapStat = new SwapStat();
            swapStat.setSwapTotal(total);
            swapStat.setSwapUsed(used);
            swapStat.setSwapPer(NumberUtil.div(used * PERCENT, total, DEFAULT_PRECISION));
            swapStat.setSwapPageIn(swap.getPageIn());
            swapStat.setSwapPageOut(swap.getPageOut());
        } catch (SigarException e) {
            log.error("get swap information fail.");
            log.error(e.getMessage(), e);
        }
        return swapStat;
    }

    public String getConnStat(int flags) {
        StringBuilder builder = new StringBuilder();
        try {
            NetConnection[] netConnectionList = SIGAR.getNetConnectionList(flags);
            for (NetConnection netConnection : netConnectionList) {
                builder.append(netConnection.toString() + "\r\n");
                log.error("conn={}", netConnection);
            }
        } catch (SigarException e) {
            log.error(e.getMessage());
        }
        return builder.toString();
    }

    public List<UscadaStat> getUscadaStat(){
        List<String> uscadaIpList = commonConfig.getUscadaIpList();
        if (!CollectionUtils.isEmpty(uscadaIpList)) {
            ArrayList<UscadaStat> uscadaStatsList = new ArrayList<>(uscadaIpList.size());
            for (String uscadaIp : uscadaIpList) {
                short status = 2;
                try {
                    JSONObject respJson = UscadaClient.getScadaStatus(uscadaIp);
                    if ("10000".equals(respJson.getString("code"))){
                        status = Short.parseShort(respJson.getString("status"));
                    }
                } catch (Exception e) {
                    status = 3;
                }
                uscadaStatsList.add(new UscadaStat(uscadaIp, status));
            }
            return uscadaStatsList;
        }
        return null;
    }

        /*public static void ethernet() throws SigarException {
        Sigar sigar = null;
        sigar = new Sigar();
        String[] ifaces = sigar.getNetInterfaceList();
        for (int i = 0; i < ifaces.length; i++) {
            NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
            if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                    || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                continue;
            }
            System.out.println(cfg.getName() + "IP地址:" + cfg.getAddress());// IP地址
            System.out.println(cfg.getName() + "网关广播地址:" + cfg.getBroadcast());// 网关广播地址
            System.out.println(cfg.getName() + "网卡MAC地址:" + cfg.getHwaddr());// 网卡MAC地址
            System.out.println(cfg.getName() + "子网掩码:" + cfg.getNetmask());// 子网掩码
            System.out.println(cfg.getName() + "网卡描述信息:" + cfg.getDescription());// 网卡描述信息
            System.out.println(cfg.getName() + "网卡类型" + cfg.getType());//
        }
    }*/
}
