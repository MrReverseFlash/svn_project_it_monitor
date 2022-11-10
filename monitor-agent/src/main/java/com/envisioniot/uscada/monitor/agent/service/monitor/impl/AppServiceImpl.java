package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.agent.component.SigarComponent;
import com.envisioniot.uscada.monitor.agent.exception.TaskException;
import com.envisioniot.uscada.monitor.agent.service.monitor.AbstractMonitorService;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.envisioniot.uscada.monitor.agent.util.Constants.*;
import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;
import static com.envisioniot.uscada.monitor.common.util.CommConstants.ONLINE_STATUS;


/**
 * @author hao.luo
 * @date 2020-12-16
 */
@Service
@Slf4j
public class AppServiceImpl extends AbstractMonitorService<CommStat<AppStat>, List<AppObj>> {

    @Autowired
    private SigarComponent sigarComponent;

    @Override
    public CommStat<AppStat> getMonitorSample(List<AppObj> appObjs) {
        if (CollectionUtils.isEmpty(appObjs)) {
            log.warn("not app uid in request for app monitor.");
            return null;
        }
        CommStat<AppStat> response = new CommStat<>();
        response.setHostIp(commonConfig.getLocalIp());
        List<AppStat> result = new ArrayList<>();
        Map<String, Set<Long>> allContainerPidMaps = getAllContainerPids();
        Set<Long> allContainerSet = new HashSet<>();
        allContainerPidMaps.values().forEach(allContainerSet::addAll);
        for (AppObj app : appObjs) {
            String containerName = app.getContainerName();
            if (StrUtil.isNotBlank(containerName)) {
                Set<Long> pids = allContainerPidMaps.get(app.getContainerName());
                if (CollectionUtils.isEmpty(pids)) {
                    pids = allContainerPidMaps.get("/" + containerName);
                }
                result.add(sigarComponent.getLoadPidByAppUid(app.getAppUid(), pids, true));
            } else {
                result.add(sigarComponent.getLoadPidByAppUid(app.getAppUid(), allContainerSet, false));
            }
        }
        // calculate memory use
        MemStat memory = sigarComponent.memory();
        double total = memory == null ? 0.0 : memory.getTotal();
        for (AppStat app : result) {
            if (app.getStatus() == ONLINE_STATUS) {
                Double memUse = app.getMemUse();
                double memPer = NumberUtil.div(memUse * PERCENT, total, PERCENT_PRECISION);
                app.setMemPer(memPer);
            }
        }
        response.setData(result);
        return response;
    }

    @Override
    public String samplePostUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.POST_APP_SAMPLE_URL;
    }

    @Override
    public String getMonitorObjUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.GET_APP_INFO_URL + "?hostIp=" + commonConfig.getLocalIp();
    }

    @Override
    public List<AppObj> convertObject(Object obj) {
        return JSONUtil.parse(obj).toBean(new TypeReference<List<AppObj>>() {
        });
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getApp();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.APP_TASK.getUuid();
    }

    private Map<String, Set<Long>> getAllContainerPids() {
        Map<String, Set<Long>> containerMaps = new HashMap<>(4);
        List<ContainerObj> dockerContainerPid = getDockerContainerPid();
        dockerContainerPid.forEach((obj) -> containerMaps.put(obj.getName(), getOneContainerInnerAllPid(obj.getName(), obj.getPid())));
        return containerMaps;
    }

    /**
     * @param exp           进程匹配表达式
     * @param containerName 容器名称
     */
    public List<RunAppResp> getOneContainerMatchApp(String exp, String containerName) throws Exception {
        List<ContainerObj> dockerContainerPid = getDockerContainerPid();
        if (CollectionUtils.isEmpty(dockerContainerPid)) {
            throw new TaskException("no run container.");
        }
        Optional<ContainerObj> first = dockerContainerPid.stream().filter(obj -> obj.getName().equalsIgnoreCase(containerName) || obj.getName().equalsIgnoreCase("/" + containerName)).findFirst();
        if (!first.isPresent()) {
            log.error("match fail containerName={}, allRunContainers={}", containerName, dockerContainerPid);
            throw new TaskException("match containerName failed.");
        }
        ContainerObj containerObj = first.get();
        Set<Long> oneContainerInnerAllPid = getOneContainerInnerAllPid(containerObj.getName(), containerObj.getPid());
        return sigarComponent.getMatchApp(exp, commonConfig.getAppUidLen(), oneContainerInnerAllPid, true);
    }

    /**
     * @param exp           进程匹配
     * @param containerName 容器名称
     * @param isDocker      是否是docker环境
     * @return 匹配的进程
     */
    public List<RunAppResp> getMatchApp(String exp, String containerName, boolean isDocker) {
        try {
            if (!isDocker) {
                return sigarComponent.getMatchApp(exp, commonConfig.getAppUidLen(), new HashSet<>(), false);
            }
            // 获取本机非容器的进程
            if (StrUtil.isBlank(containerName)) {
                List<ContainerObj> dockerContainerPid = getDockerContainerPid();
                Set<Long> allDockerProcess = new HashSet<>();
                for (ContainerObj containerObj : dockerContainerPid) {
                    allDockerProcess.addAll(getOneContainerInnerAllPid(containerObj.getName(), containerObj.getPid()));
                }
                return sigarComponent.getMatchApp(exp, commonConfig.getAppUidLen(), allDockerProcess, false);
            } else {
                return getOneContainerMatchApp(exp, containerName);
            }
        } catch (Exception e) {
            String msg = String.format("get match app fail, exp=%s.", exp);
            log.error(msg);
            log.error(e.getMessage(), e);
            throw new TaskException(msg);
        }
    }

    /**
     * @param containerName 容器名称
     * @return 根据容器名称获取所有的容器所有进程pid, 除开自身的id
     */
    public Set<Long> getOneContainerInnerAllPid(String containerName, Long containerPid) {
        String[] cmd = {"docker", "top", containerName};
        Set<Long> pidSet = new HashSet<>();
        List<String> topCmdRes = null;
        try {
            topCmdRes = RuntimeUtil.execForLines(cmd);
            if (CollectionUtils.isEmpty(topCmdRes) || topCmdRes.size() == 1) {
                return pidSet;
            }
            int subLen = DOCKER_TOP_PID_TAG.length();
            int index = getIndex(topCmdRes.get(0));
            if (index == -1) {
                log.error("docker top not found tag={}", DOCKER_TOP_PID_TAG);
                return pidSet;
            }
            for (int i = 1; i < topCmdRes.size(); i++) {
                Long pid = getPid(topCmdRes.get(i), index, subLen);
                if (!pid.equals(containerPid)) {
                    pidSet.add(pid);
                }
            }
        } catch (Exception e) {
            log.error("cmd={} result={}", String.join(" ", cmd), topCmdRes);
            log.error("run cmd={} failed", String.join(" ", cmd));
            log.error(e.getMessage(), e);
        }
        return pidSet;
    }

    /**
     * @param jsonString docker top每行结果
     * @param index      pid开始位置
     * @param tagLen     pid长度
     * @return 返回容器里面的进程pid
     */
    private Long getPid(String jsonString, int index, int tagLen) {
        int length = jsonString.length();
        int st = index, et = index;
        for (int i = 0; i < tagLen; i++) {
            int pos = index + i;
            if (Character.isDigit(jsonString.charAt(index + i))) {
                et = st = pos;
                break;
            }
        }
        while (st >= 0) {
            if (Character.isDigit(jsonString.charAt(st))) {
                st--;
            } else {
                break;
            }
        }
        while (et < length) {
            if (Character.isDigit(jsonString.charAt(et))) {
                et++;
            } else {
                break;
            }
        }
        if (st >= 0 && !Character.isDigit(jsonString.charAt(st))) {
            st = st + 1;
        }
        if (et < length && !Character.isDigit(jsonString.charAt(et))) {
            et = et - 1;
        }
        return Long.parseLong(jsonString.substring(st, et + 1));
    }

    /**
     * @param topCmdRes docker top的名称返回首行
     * @return PID的所在index
     */
    private int getIndex(String topCmdRes) {
        String tags = topCmdRes.toUpperCase();
        int subLen = DOCKER_TOP_PID_TAG.length();
        int length = tags.length();
        int i = -1;
        for (; (i = tags.indexOf(DOCKER_TOP_PID_TAG, i + 1)) != -1; i++) {
            if (i - 1 >= 0 && i + subLen < length) {
                if (tags.charAt(i - 1) == ' ' && tags.charAt(i + subLen) == ' ') {
                    return i;
                }
            } else if (i == 0 && i + subLen == length) {
                return i;
            } else if (i == 0 && i + subLen < length && tags.charAt(i + subLen) == ' ') {
                return i;
            } else if (i - 1 >= 0 && i + subLen == length && tags.charAt(i - 1) == ' ') {
                return i;
            }
        }
        return i;
    }

    /**
     * @return 获取所有正在运行容器的名称和pid
     */
    public List<ContainerObj> getDockerContainerPid() {
        List<ContainerObj> runContainers = new ArrayList<>();
        List<String> containers = null;
        try {
            containers = RuntimeUtil.execForLines(DOCKER_CONTAINER_ID);
            String[] cmd = new String[DOCKER_PID.length + containers.size()];
            final int[] i = {0};
            for (; i[0] < DOCKER_PID.length; i[0]++) {
                cmd[i[0]] = DOCKER_PID[i[0]];
            }
            containers.forEach(id -> {
                cmd[i[0]++] = id;
            });
            containers = RuntimeUtil.execForLines(cmd);
            if (CollectionUtils.isEmpty(containers)) {
                log.error("found zero run container.");
                return runContainers;
            }
            containers.forEach((jsonString) -> {
                if (StrUtil.isNotBlank(jsonString)) {
                    ContainerObj containerObj = JSONUtil.parse(jsonString).toBean(new TypeReference<ContainerObj>() {
                    });
                    runContainers.add(containerObj);
                }
            });
        } catch (Exception e) {
            log.error("cmd={} result={}", String.join(" ", DOCKER_PID), containers);
            log.error("get all run docker pid failed");
            log.error(e.getMessage(), e);
        }
        return runContainers;
    }

    public static void main(String[] args) {
        AppServiceImpl appService = new AppServiceImpl();
        List<String> topCmdRes = new ArrayList<>();
        topCmdRes.add("UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD\n");
        topCmdRes.add("polkitd             2059                2036                11                  8月17                pts/0               02:54:08            mysqld\n");
        topCmdRes.add("windos              31336               21426               8                   11:36               ?                   00:01:25            /usr/local/jdk1.8.0_211/jre/bin/java -Dnop -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Xms256m -Xmx512m -XX:PermSize=64M -XX:MaxPermSize=128M -Djdk.tls.ephemeralDHKeySize=2048 -Dorg.apache.catalina.security.SecurityListener.UMASK=0027 -Dignore.endorsed.dirs= -classpath /home/windos/envision/tomcat7/bin/bootstrap.jar:/home/windos/envision/tomcat7/bin/tomcat-juli.jar -Dcatalina.base=/home/windos/envision/tomcat7 -Dcatalina.home=/home/windos/envision/tomcat7 -Djava.io.tmpdir=/home/windos/envision/tomcat7/temp org.apache.catalina.startup.Bootstrap start\n");
        topCmdRes.add("windos              31453               21426               1                   11:36               ?                   00:00:17            /home/windos/envision/bin/report_svr -webservice\n");
        topCmdRes.add("windos              19296               13394               1                   12:45               ?                   00:02:02            /usr/local/jdk1.8.0_211/jre/bin/java -Dnop -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Xms256m -Xmx512m -XX:PermSize=64M -XX:MaxPermSize=128M -Djdk.tls.ephemeralDHKeySize=2048 -Dorg.apache.catalina.security.SecurityListener.UMASK=0027 -Dignore.endorsed.dirs= -classpath /home/windos/envision/tomcat7/bin/bootstrap.jar:/home/windos/envision/tomcat7/bin/tomcat-juli.jar -Dcatalina.base=/home/windos/envision/tomcat7 -Dcatalina.home=/home/windos/envision/tomcat7 -Djava.io.tmpdir=/home/windos/envision/tomcat7/temp org.apache.catalina.startup.Bootstrap start");
        topCmdRes.add("windos              19144               13394               0                   12:45               ?                   00:00:00            /home/windos/envision/bin/hdb_schedule_task");
        Set<Long> pidSet = new HashSet<>();
        try {
            int subLen = DOCKER_TOP_PID_TAG.length();
            int index = appService.getIndex(topCmdRes.get(0));
            if (index == -1) {
                System.out.println("index = -1");
            }
            for (int i = 1; i < topCmdRes.size(); i++) {
                pidSet.add(appService.getPid(topCmdRes.get(i), index, subLen));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(pidSet);

        List<String> containers = new ArrayList<>();
        containers.add("sghgh");
        String[] cmd = new String[DOCKER_PID.length + containers.size()];
        final int[] i = {0};
        for (; i[0] < DOCKER_PID.length; i[0]++) {
            cmd[i[0]] = DOCKER_PID[i[0]];
        }
        containers.forEach(id -> {
            cmd[i[0]++] = id;
        });
        System.out.println(String.join(" ", cmd));
    }
}
