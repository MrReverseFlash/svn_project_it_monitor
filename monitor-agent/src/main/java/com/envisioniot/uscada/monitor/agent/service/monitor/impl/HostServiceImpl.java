package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.HostInfo;
import com.envisioniot.uscada.monitor.agent.component.OshiComponent;
import com.envisioniot.uscada.monitor.agent.component.SigarComponent;
import com.envisioniot.uscada.monitor.agent.service.monitor.AbstractMonitorService;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.ProcStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.envisioniot.uscada.monitor.agent.util.Constants.*;
import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;


/**
 * @author hao.luo
 * @date 2020-12-15
 */
@Service
@Slf4j
public class HostServiceImpl extends AbstractMonitorService<HostStat, Object> {

    @Autowired
    private SigarComponent sigarComponent;

    @Autowired
    private OshiComponent oshiComponent;

    @Value("${server.port}")
    private String agentPort;

    @Override
    public HostStat getMonitorSample(Object request) {
        HostStat hostStat = new HostStat();
        hostStat.setSystemInfo(sigarComponent.os());
        if (hostStat.getSystemInfo() != null) {
            ProcStat cpuState = sigarComponent.getState();
            if (cpuState != null) {
                hostStat.getSystemInfo().setProcessNum(cpuState.getTotal());
                hostStat.getSystemInfo().setZombieNum(cpuState.getZombie());
            }
        }
        hostStat.setMemSample(sigarComponent.memory());
        hostStat.setSwapSample(sigarComponent.swap());
        hostStat.setCpuSample(sigarComponent.cpu());
        hostStat.setSysLoadSample(sigarComponent.getSysLoad());
        hostStat.setUscadaStatList(sigarComponent.getUscadaStat());

        // net card sample
        List<NetIoStat> netSample = getNetSample();
        hostStat.setNetSample(netSample);
        hostStat.setNetTotal(getNetTotal(netSample));
        List<DiskStat> diskStats = getDiskSample();
        hostStat.setDiskList(diskStats);
        hostStat.setDiskTotal(calcDiskTotal(diskStats));
        hostStat.setRunTime(sigarComponent.getHostUptime());
        DateTime now = DateTime.now();
        hostStat.setOccurTime(now.toString());
        hostStat.setStartTime(DateUtil.offset(now, DateField.SECOND, -hostStat.getRunTime()).toString());
        hostStat.setHostIp(commonConfig.getLocalIp());
        hostStat.setPort(agentPort);
        hostStat.setHostName(getHostName() == null ? System.getenv("HOSTNAME") : getHostName());
        log.info("getHostName : {}", hostStat.getHostName());
        log.info("ENV HOSTNAME : {}", System.getenv("HOSTNAME"));
        List<WhoStat> who = sigarComponent.who();
        if (who != null) {
            hostStat.setWhoList(who);
            hostStat.setLogUserNum(who.size());
        }
        hostStat.setStatus(CommConstants.ONLINE_STATUS);
        return hostStat;
    }

    /**
     * @param diskStats all disk sample
     * @return 主机disk的统计信息
     */
    private DiskTotalInfo calcDiskTotal(List<DiskStat> diskStats) {
        if (CollectionUtil.isEmpty(diskStats)) {
            return null;
        }
        long useNodes = 0, totalNodes = 0;
        double use = 0, total = 0, diskPer = 0.0, inodePer = 0.0;
        for (DiskStat diskStat : diskStats) {
            if (commonConfig.getFilterOverlay() && diskStat.getOverlay()) {
                continue;
            }
            useNodes += diskStat.getUseNodes();
            totalNodes += diskStat.getTotalNodes();
            use += diskStat.getUsed();
            total += diskStat.getSize();
        }
        if (totalNodes > 0) {
            inodePer = NumberUtil.div(useNodes * PERCENT, totalNodes, PERCENT_PRECISION);
        }
        if (total > 0) {
            diskPer = NumberUtil.div(use * PERCENT, total, PERCENT_PRECISION);
        }
        DiskTotalInfo diskTotal = new DiskTotalInfo();
        diskTotal.setInodesPer(inodePer);
        diskTotal.setDiskPer(diskPer);
        diskTotal.setDiskUse(use);
        diskTotal.setDiskTotal(total);
        return diskTotal;
    }

    /**
     * 获取主机网络的统计信息
     */
    private NetTotalInfo getNetTotal(List<NetIoStat> netSample) {
        if (CollectionUtil.isEmpty(netSample)) {
            return null;
        }
        double totalBandWidth = 0.0, totalNetFlow = 0.0, netMaxPer = 0.0, netPer = 0.0;
        for (NetIoStat card : netSample) {
            totalBandWidth += card.getTotalBandWidth();
            totalNetFlow += card.getTotalNetFlow();
            if (card.getTotalBandWidth() > 0.0) {
                netPer = NumberUtil.div(card.getTotalNetFlow() * 100.0, card.getTotalBandWidth().doubleValue(), PERCENT_PRECISION);
            }
            if (netMaxPer < netPer) {
                netMaxPer = netPer;
            }
        }
        NetTotalInfo netTotal = new NetTotalInfo();
        netTotal.setNetMaxPer(netMaxPer);
        netTotal.setTotalBandWidth(totalBandWidth);
        netTotal.setTotalNetFlow(totalNetFlow);
        if (netTotal.getTotalBandWidth() > 0) {
            netPer = NumberUtil.div(netTotal.getTotalNetFlow() * 100.0, netTotal.getTotalBandWidth().doubleValue(), PERCENT_PRECISION);
            netTotal.setNetPer(netPer);
        } else {
            netTotal.setNetPer(0.0);
        }
        return netTotal;
    }

    /**
     * @return 获取所有网卡的采样信息
     */
    private List<NetIoStat> getNetSample() {
        try {
            Map<String, NetIoStat> netMap = oshiComponent.net();
            long st = System.currentTimeMillis();
            TimeUnit.MILLISECONDS.sleep(commonConfig.getNetInterval());
            Collection<NetIoStat> values = oshiComponent.net().values();
            if (CollectionUtil.isEmpty(values)) {
                return null;
            }
            long et = System.currentTimeMillis();
            long interval = et - st;
            List<NetIoStat> netCards = new ArrayList<>(8);
            values.forEach(card -> {
                String ifName = card.getIfName();
                NetIoStat last = netMap.get(ifName);
                if (last == null) {
                    return;
                }
                card.setTotalNetOut(NumberUtil.div((card.getTxbyt() - last.getTxbyt()) * 8000, interval, DEFAULT_PRECISION));
                card.setTotalNetIn(NumberUtil.div((card.getRxbyt() - last.getRxbyt()) * 8000, interval, DEFAULT_PRECISION));
                card.setTotalNetFlow(card.getTotalNetIn() + card.getTotalNetOut());
                netCards.add(card);
            });
            return netCards;
        } catch (Exception e) {
            log.error("get net card sample fail.");
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @return 获取所有磁盘采样信息
     */
    public List<DiskStat> getDiskSample() {
        try {
            Boolean filterVirtualDisk = commonConfig.getFilterVirtualDisk();
            Map<String, DiskStat> statMap = sigarComponent.diskInfo(filterVirtualDisk);
            long st = System.currentTimeMillis();
            TimeUnit.MILLISECONDS.sleep(commonConfig.getDiskInterval());
            Collection<DiskStat> values = sigarComponent.diskInfo(filterVirtualDisk).values();
            if (CollectionUtil.isEmpty(values)) {
                return null;
            }
            long et = System.currentTimeMillis();
            long interval = et - st;
            List<DiskStat> diskStats = new ArrayList<>(8);
            values.forEach(disk -> {
                String name = disk.getDirName();
                DiskStat last = statMap.get(name);
                if (last == null) {
                    return;
                }
                disk.setReadRate(NumberUtil.div((disk.getReadBytes() - last.getReadBytes()) / 1.024, interval, DEFAULT_PRECISION));
                disk.setWriteRate(NumberUtil.div((disk.getWriteBytes() - last.getWriteBytes()) / 1.024, interval, DEFAULT_PRECISION));
                diskStats.add(disk);
            });
            return diskStats;
        } catch (Exception e) {
            log.error("get disk sample fail.");
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String samplePostUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.POST_HOST_SAMPLE_URL;
    }

    @Override
    public String getMonitorObjUrl() {
        return null;
    }

    @Override
    public Object convertObject(Object obj) {
        return null;
    }

    private String getHostName() {
        return new HostInfo().getName();
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getHost();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.HOST_TASK.getUuid();
    }
}
