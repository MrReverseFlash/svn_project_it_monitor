package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.agent.component.SigarComponent;
import com.envisioniot.uscada.monitor.agent.service.monitor.AbstractMonitorService;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;

/**
 * @author hao.luo
 * @date 2020-12-16
 */
@Service
@Slf4j
public class PortServiceImpl extends AbstractMonitorService<CommStat<PortStatSample>, List<PortInfo>> {

    @Autowired
    private SigarComponent sigarComponent;

    @Override
    public CommStat<PortStatSample> getMonitorSample(List<PortInfo> portList) {
        if (CollectionUtils.isEmpty(portList)) {
            log.warn("not port in request for port monitor.");
            return null;
        }
        CommStat<PortStatSample> response = new CommStat<>();
        response.setHostIp(commonConfig.getLocalIp());
        List<PortStatSample> portSamples = getPortSample(portList);
        response.setData(portSamples);
        return response;
    }

    public List<PortStatSample> getPortSample(List<PortInfo> portList) {
        List<PortStatSample> portSamples = new ArrayList<>(4);
        Map<Long, PortNetStat> localPortNetStat = sigarComponent.getLocalPortNetStat();
        String now = DateUtil.now();
        for (PortInfo portInfo : portList) {
            String port = portInfo.getPortNum();
            PortNetStat portNetStat = localPortNetStat.get(Long.valueOf(port));
            PortStatSample portStatSample;
            if (portNetStat == null) {
                portStatSample = new PortStatSample();
                portStatSample.setStatus(CommConstants.OFFLINE_STATUS);
                portStatSample.setMsg("port not exist!");
            } else {
                portStatSample = new PortStatSample(portNetStat);
            }
            portStatSample.setPortNum(port);
            portStatSample.setOccurTime(now);
            portSamples.add(portStatSample);
        }
        return portSamples;
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getPort();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.PORT_TASK.getUuid();
    }

    @Override
    public String samplePostUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.POST_PORT_SAMPLE_URL;
    }

    @Override
    public String getMonitorObjUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.GET_PORT_INFO_URL + "?hostIp=" + commonConfig.getLocalIp();
    }

    @Override
    public List<PortInfo> convertObject(Object obj) {
        return JSONUtil.parse(obj).toBean(new TypeReference<List<PortInfo>>() {
        });
    }
}
