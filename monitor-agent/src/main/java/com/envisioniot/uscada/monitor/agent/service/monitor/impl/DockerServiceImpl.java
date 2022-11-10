package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.agent.service.monitor.AbstractMonitorService;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.common.entity.ContainerStat;
import com.envisioniot.uscada.monitor.common.entity.TaskEnum;
import com.envisioniot.uscada.monitor.common.entity.TaskInfo;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.envisioniot.uscada.monitor.agent.util.Constants.*;
import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;
import static java.util.stream.Collectors.toList;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
@Service
@Slf4j
public class DockerServiceImpl extends AbstractMonitorService<CommStat<ContainerStat>, List<String>> {

    @Override
    public CommStat<ContainerStat> getMonitorSample(List<String> containers) {
        if (CollectionUtils.isEmpty(containers)) {
            log.warn("not container in request for monitor.");
            return null;
        }
        CommStat<ContainerStat> response = new CommStat<>();
        response.setHostIp(commonConfig.getLocalIp());
        try {
            List<ContainerStat> monitorContainer = getMonitorContainer(containers);
            List<ContainerStat> containerHealth = getContainerHealth();
            String now = DateUtil.now();
            monitorContainer.forEach((container) -> {
                String id = container.getId();
                ContainerStat statMatch = null;
                for (ContainerStat stat : containerHealth) {
                    if (stat.getId().startsWith(id)) {
                        statMatch = stat;
                        break;
                    }
                }
                if (statMatch != null) {
                    // 进程个数
                    container.setPids(statMatch.getPids());
                    // 内存使用率
                    container.setMemPer(statMatch.getMemPer());
                    // cpu使用率
                    container.setCpuPer(statMatch.getCpuPer());
                    // net IO
                    container.setNetIoSend(statMatch.getNetIoSend());
                    container.setNetIoReceive(statMatch.getNetIoReceive());
                    // block io
                    container.setBlockIoRead(statMatch.getBlockIoRead());
                    container.setBlockIoWrite(statMatch.getBlockIoWrite());
                } else {
                    container.setMsg("container no found.");
                    container.setStatus(CommConstants.OFFLINE_STATUS);
                }
                container.setOccurTime(now);
            });
            response.setData(monitorContainer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    private List<ContainerStat> getMonitorContainer(List<String> containers) {
        List<ContainerStat> result = new ArrayList<>(2);
        List<ContainerStat> allContainer = getAllContainer();
        List<String> allContainerIds = allContainer.stream().map(containerStat -> containerStat.getId()).collect(toList());
        List<String> deadContainerIds = containers.stream().filter(containerId -> !allContainerIds.contains(containerId)).collect(toList());
        result.addAll(allContainer);
        for (String id : deadContainerIds) {
            ContainerStat stat = new ContainerStat();
            stat.setId(id);
            result.add(stat);
        }
        log.info("result size :"+result.size());

        return result;
    }

    private List<ContainerStat> getAllContainer() {
        List<ContainerStat> containerStats = new ArrayList<>();
        List<String> containers = RuntimeUtil.execForLines(DOCKER_PS);
        if (CollectionUtils.isEmpty(containers)) {
            log.error("found zero container.");
            return containerStats;
        }
        containers.forEach((container) -> {
            JSONObject jsonObject = JSONUtil.parseObj(container);
            ContainerStat containerStat = convertToContainer(jsonObject);
            containerStats.add(containerStat);
        });
        return containerStats;
    }

    private ContainerStat convertToContainer(JSONObject jsonObject) {
        ContainerStat containerStat = new ContainerStat();
        // 容器id
        //改为name
        containerStat.setId(jsonObject.getStr(CONTAINER_NAME));
        // 容器的名称
        containerStat.setName(jsonObject.getStr(CONTAINER_NAME));
        // 容器的镜像
        containerStat.setImage(jsonObject.getStr(CONTAINER_IMAGE));
        // 当前容器的状态
        String status = jsonObject.getStr(CONTAINER_STATUS);
        containerStat.setStatus(getContainerStat(status));
        // 容器的上次启动时间
        containerStat.setUpTime(status);
        // 容器的创建时间
        containerStat.setContainerCreateTime(jsonObject.getStr(CONTAINER_CREATE_TIME));
        return containerStat;
    }

    private Integer getContainerStat(String status) {
        if (status != null && status.toLowerCase().contains("up")) {
            return CommConstants.ONLINE_STATUS;
        } else {
            return CommConstants.OFFLINE_STATUS;
        }
    }

    private List<ContainerStat> getContainerHealth() {
        List<ContainerStat> containerStats = new ArrayList<>();
        List<String> containers = RuntimeUtil.execForLines(DOCKER_STATS);
        if (CollectionUtils.isEmpty(containers)) {
            log.error("found zero container.");
            return containerStats;
        }
        containers.forEach((container) -> {
            JSONObject jsonObject = JSONUtil.parseObj(container);
            ContainerStat containerStat = convertToContainerHealth(jsonObject);
            containerStats.add(containerStat);
        });
        return containerStats;
    }

    private ContainerStat convertToContainerHealth(JSONObject jsonObject) {
        ContainerStat containerStat = new ContainerStat();
        // 容器id
        //执行命令时 实际已经改成使用name
        containerStat.setId(jsonObject.getStr(CONTAINER_ID));
        // 进程个数
        String pids = jsonObject.getStr(CONTAINER_PIDS);
        containerStat.setPids(StringUtils.isEmpty(pids) ? 0L : Long.parseLong(pids));
        // 内存使用率
        String memPer = jsonObject.getStr(CONTAINER_MEMORY_PER);
        containerStat.setMemPer(StringUtils.isEmpty(memPer) ? 0.0 : Double.parseDouble(memPer.split("%")[0]));
        // cpu使用率
        String cpuPer = jsonObject.getStr(CONTAINER_CPU_PER);
        containerStat.setCpuPer(StringUtils.isEmpty(cpuPer) ? 0.0 : Double.parseDouble(cpuPer.split("%")[0]));
        // net IO
        List<Long> io = parseIo(jsonObject.getStr(CONTAINER_NET_IO));
        containerStat.setNetIoSend(io.get(0));
        containerStat.setNetIoReceive(io.get(1));
        // block io
        io = parseIo(jsonObject.getStr(CONTAINER_BLOCK_IO));
        containerStat.setBlockIoRead(io.get(0));
        containerStat.setBlockIoWrite(io.get(1));
        return containerStat;
    }

    private List<Long> parseIo(String io) {
        if (StringUtils.isEmpty(io)) {
            log.error(" io={} is empty.", io);
            return Arrays.asList(0L, 0L);
        }

        String[] split = io.split("/");
        if (split.length != 2) {
            log.error(" io={} is not format A/B", io);
            return Arrays.asList(0L, 0L);
        }
        Long ioValKb = getIoValKb(split[0]);
        Long ioValKb1 = getIoValKb(split[1]);
        return Arrays.asList(ioValKb, ioValKb1);
    }

    private Long getIoValKb(String ioVal) {
        String trim = ioVal.trim().toUpperCase();
        String val;
        if (trim.contains("KB")) {
            val = trim.split("KB")[0].trim();
            return  Double.valueOf(val).longValue();
        } else if (trim.contains("MB")) {
            val = trim.split("MB")[0].trim();
            return Double.valueOf(val).longValue() * 1024;
        } else if (trim.contains("GB")) {
            val = trim.split("GB")[0].trim();
            return Double.valueOf(val).longValue() * 1024 * 1024;
        } else if (trim.contains("TB")) {
            val = trim.split("TB")[0].trim();
            return Double.valueOf(val).longValue() * 1024 * 1024 * 1024;
        } else if (trim.contains("PB")) {
            val = trim.split("PB")[0].trim();
            return Double.valueOf(val).longValue() * 1024 * 1024 * 1024 * 1024;
        } else if (trim.contains("EB")) {
            val = trim.split("EB")[0].trim();
            return Double.valueOf(val).longValue() * 1024 * 1024 * 1024 * 1024 * 1024;
        } else if (trim.contains("B")) {
            val = trim.split("B")[0].trim();
            return Double.valueOf(val).longValue() / 1024;
        } else {
            log.error("io = {} is not valid.", ioVal);
            return 0L;
        }
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getDocker();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.DOCKER_TASK.getUuid();
    }

    @Override
    public String samplePostUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.POST_DOCKER_SAMPLE_URL;
    }

    @Override
    public String getMonitorObjUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.GET_DOCKER_INFO_URL + "?hostIp=" + commonConfig.getLocalIp();
    }

    @Override
    public List<String> convertObject(Object obj) {
        return JSONUtil.parse(obj).toBean(new TypeReference<List<String>>() {
        });
    }

    /**
     * 默认监控所有docker容器
     */
    @Override
    public void initMonitor() {
        try {
            CommStat<ContainerStat> response = new CommStat<>();
            response.setHostIp(commonConfig.getLocalIp());
            List<ContainerStat> allContainer = getAllContainer();
            if (CollectionUtils.isEmpty(allContainer)) {
                log.warn("no default container to monitor.");
                return;
            }
            String now = DateUtil.now();
            allContainer.forEach((containerStat -> {
                containerStat.setOccurTime(now);
            }));
            response.setData(allContainer);
            pushInitMonitorObj(response);
        } catch (Exception e) {
            log.error("init monitor task={} failed.", getTaskInfo());
            log.error(e.getMessage(), e);
        }
    }
}
