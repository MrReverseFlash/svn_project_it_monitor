package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.agent.component.SigarComponent;
import com.envisioniot.uscada.monitor.agent.mapper.ScadaMapper;
import com.envisioniot.uscada.monitor.agent.service.monitor.AbstractMonitorService;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;

/**
 * @author hao.luo
 * @date 2020-12-18
 */
@Service
@Slf4j
public class IedServiceImpl extends AbstractMonitorService<CommStat<IedStat>, List<IedInfo>> {

    @Autowired
    private SigarComponent sigarComponent;

    @Autowired
    private ScadaMapper scadaMapper;

    @Override
    public CommStat<IedStat> getMonitorSample(List<IedInfo> iedInfos) {
        if (CollectionUtils.isEmpty(iedInfos)) {
            log.warn("not ied in request for ied monitor.");
            return null;
        }
        CommStat<IedStat> response = new CommStat<>();
        response.setHostIp(commonConfig.getLocalIp());
        List<IedStat> iedSamples = getIedSample(iedInfos);
        response.setData(iedSamples);
        return response;
    }

    private List<IedStat> getIedSample(List<IedInfo> iedInfoList) {
        Map<PeerInfo, Integer> peers = new HashMap<>(4);
        iedInfoList.forEach(iedInfo -> peers.put(new PeerInfo(iedInfo.getPeerIp(), iedInfo.getPort()), iedInfo.getCommType()));

        Map<PeerInfo, PortNetStat> remoteNetStat = sigarComponent.getRemoteNetStat(peers);
        List<IedStat> iedSample = new ArrayList<>(4);
        String now = DateUtil.now();
        iedInfoList.forEach(iedInfo -> {
            PortNetStat portNetStat = remoteNetStat.get(iedInfo);
            IedStat iedStat = new IedStat();
            BeanUtils.copyProperties(iedInfo, iedStat);
            if (portNetStat == null) {
                iedStat.setStatus(CommConstants.OFFLINE_STATUS);
                iedStat.setMsg("port not exist!");
            } else {
                iedStat.setStatus(portNetStat.getFinalStatus());
            }
            iedStat.setOccurTime(now);
            iedSample.add(iedStat);
        });
        return iedSample;
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getIed();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.IED_TASK.getUuid();
    }

    @Override
    public String samplePostUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.POST_IED_SAMPLE_URL;
    }

    @Override
    public String getMonitorObjUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.GET_IED_INFO_URL + "?hostIp=" + commonConfig.getLocalIp();
    }

    @Override
    public List<IedInfo> convertObject(Object obj) {
        return JSONUtil.parse(obj).toBean(new TypeReference<List<IedInfo>>() {
        });
    }

    /**
     * 默认监控所有数据库中的ied设备
     */
    @Override
    public void initMonitor() {
        try {
            CommStat<IedStat> response = new CommStat<>();
            response.setHostIp(commonConfig.getLocalIp());
            List<IedStat> allIedInfo = scadaMapper.getAllIedInfo();
            Iterator<IedStat> iterator = allIedInfo.iterator();
            Set<Integer> serverCommType = commonConfig.getServerCommType();
            Set<Integer> clientCommType = commonConfig.getClientCommType();
            while (iterator.hasNext()) {
                IedStat next = iterator.next();
                String peerIp = next.getPeerIp();
                String port = next.getPort();
                if (StringUtils.isEmpty(peerIp) || StringUtils.isEmpty(port)) {
                    log.warn("ied={} has no net_port_1 and ip1", next);
                    iterator.remove();
                    continue;
                }
                Integer commType = next.getCommType();
                if (commType == null || (!serverCommType.contains(commType) && !clientCommType.contains(commType))) {
                    log.warn("ied={} is not server or client, not to monitor", next);
                    iterator.remove();
                    continue;
                }
                String stringIp = convertIp(Integer.parseInt(peerIp));
                next.setPeerIp(stringIp);
            }
            if (CollectionUtils.isEmpty(allIedInfo)) {
                log.warn("no default ied to monitor.");
                return;
            }
            response.setData(allIedInfo);
            pushInitMonitorObj(response);
        } catch (Exception e) {
            log.error("init monitor task={} failed.", getTaskInfo());
            log.error(e.getMessage(), e);
        }
    }

    private String convertIp(int val) {
        long unsignVal = Integer.toUnsignedLong(val);
        return String.format("%d.%d.%d.%d", unsignVal / 256 / 256 / 256, unsignVal / 256 / 256 % 256, unsignVal / 256 % 256, unsignVal % 256);
    }


    public static void main(String[] args) {
        long a = Integer.toUnsignedLong(Integer.parseInt("-1408229028"));
        System.out.println(a / 256 / 256 / 256);
        System.out.println(a / 256 / 256 % 256);
        System.out.println(a / 256 % 256);
        System.out.println(a % 256);
    }
}
