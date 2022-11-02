package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.agent.mapper.ScadaMapper;
import com.envisioniot.uscada.monitor.agent.service.monitor.AbstractMonitorService;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;

/**
 * @author YK
 */
@Service
@Slf4j
public class TopoServiceImpl extends AbstractMonitorService<CommStat<TopoInfo>, List<String>> {

    /**
     * #1. 网关状态信息：
     * 根据缓存的网关列表，检测它们的状态，并传至服务端。
     * #2. 拓扑连接状态信息：
     * 根据从服务端获取的最新连接列表，检测它们的状态，并传至服务端。
     *
     * @param relaIpList
     * @return
     */
    @Override
    public CommStat<TopoInfo> getMonitorSample(List<String> relaIpList) {
        CommStat<TopoInfo> reqBody = new CommStat<>();
        reqBody.setHostIp(commonConfig.getLocalIp());
        TopoInfo topoInfo = new TopoInfo();
        try{
            //网关状态信息
            if (!CollectionUtils.isEmpty(gatewayIpList)) {
                List<GatewayInfo> gatewayInfoList = gatewayIpList.parallelStream().map(gatewayIp -> {
                    String[] tmpGetPingMillis = Arrays.copyOf(GET_PING_MILLIS, GET_PING_MILLIS.length);
                    tmpGetPingMillis[2] = tmpGetPingMillis[2].replaceFirst("x.x.x.x", gatewayIp);
                    GatewayInfo gatewayInfo = new GatewayInfo();
                    gatewayInfo.setGatewayIp(gatewayIp);
                    try {
                        List<String> pingMillis = RuntimeUtil.execForLines(tmpGetPingMillis);
                        if (!CollectionUtils.isEmpty(pingMillis)) {
                            gatewayInfo.setStatus((short) 1);
                            gatewayInfo.setOperationMillis(Short.valueOf(String.valueOf(Double.valueOf(pingMillis.get(0)).intValue())));
                        } else {
                            gatewayInfo.setStatus((short) 0);
                        }
                    } catch (Exception e){
                        log.error(e.getMessage(), e);
                        gatewayInfo.setStatus((short) 0);
                    }
                    return gatewayInfo;
                }).collect(Collectors.toList());
                topoInfo.setGatewayInfoList(gatewayInfoList);
            }

            //连线状态信息
            if (!CollectionUtils.isEmpty(relaIpList)) {
                List<TopoRelaInfo> relaInfoList = relaIpList.parallelStream().map(relaIp -> {
                    String[] tmpGetPingMillis = Arrays.copyOf(GET_PING_MILLIS, GET_PING_MILLIS.length);
                    tmpGetPingMillis[2] = tmpGetPingMillis[2].replaceFirst("x.x.x.x", relaIp);
                    TopoRelaInfo topoRelaInfo = new TopoRelaInfo();
                    topoRelaInfo.setHostIpTarget(relaIp);
                    try {
                        List<String> pingMillis = RuntimeUtil.execForLines(tmpGetPingMillis);
                        if (!CollectionUtils.isEmpty(pingMillis)) {
                            topoRelaInfo.setStatus((short) 1);
                            topoRelaInfo.setOperationMillis(Short.valueOf(String.valueOf(Double.valueOf(pingMillis.get(0)).intValue())));
                        } else {
                            topoRelaInfo.setStatus((short) 0);
                        }
                    } catch (Exception e){
                        log.error(e.getMessage(), e);
                        topoRelaInfo.setStatus((short) 0);
                    }
                    return topoRelaInfo;
                }).collect(Collectors.toList());
                topoInfo.setRelaInfoList(relaInfoList);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //更新至远程
        reqBody.setData(Collections.singletonList(topoInfo));
        return reqBody;
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getTopo();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.TOPO_TASK.getUuid();
    }

    @Override
    public String samplePostUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.POST_TOPO_SAMPLE_URL;
    }

    @Override
    public String getMonitorObjUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.GET_TOPO_INFO_URL + "?hostIp=" + commonConfig.getLocalIp();
    }

    @Override
    public List<String> convertObject(Object obj) {
        return JSONUtil.parse(obj).toBean(new TypeReference<List<String>>() {
        });
    }

    /**
     * #1: 网关信息：
     * 启动时，查询出该主机的网关信息后，全量更新至服务端，并缓存在本地。下一次定时任务直接拿缓存的网关信息检测后更新至服务端。
     * #2: 拓扑集合关系：
     * 启动时，根据system.sys、DB_HOST生成集合关系，全量更新至服务端。
     * #3: 拓扑连接信息：
     * 启动时，根据system.sys、DB_HOST和ied_info生成默认连接关系，全量更新默认关系到服务端。下一次定时任务再查询出所有的拓扑连接信息检测后更新至服务端。
     */
    @Override
    public void initMonitor() {
        try {
            CommStat<TopoInfo> reqBody = new CommStat<>();
            String hostIp = commonConfig.getLocalIp();
            reqBody.setHostIp(hostIp);
            TopoInfo topoInfo = new TopoInfo();

            //获取网关IP
            try {
                gatewayIpList = RuntimeUtil.execForLines(GET_GATEWAY_LIST);
            } catch (Exception e){
                log.error(e.getMessage(), e);
            }
            if (!CollectionUtils.isEmpty(gatewayIpList)) {
                //每次调用bash命令都要校验一下返回的格式是否正确，格式不正确需要记录日志并丢弃结果。
                // 通过第一个元素的形式判断是否正确获取到网关IP
                String firstIp = gatewayIpList.get(0);
                if (!firstIp.matches(IP_PATTERN)) {
                    log.error("Gateway ip format is not correct: " + firstIp);
                    gatewayIpList.clear();
                } else {
                    HashSet<String> gatewaySet = new HashSet<>(gatewayIpList);
                    gatewayIpList.clear();
                    gatewayIpList.addAll(gatewaySet);
                    List<GatewayInfo> gatewayInfoList = gatewayIpList.parallelStream().map(gatewayIp -> {
                        GatewayInfo gatewayInfo = new GatewayInfo();
                        gatewayInfo.setGatewayIp(gatewayIp);
                        return gatewayInfo;
                    }).collect(Collectors.toList());
                    topoInfo.setGatewayInfoList(gatewayInfoList);
                }
            }

            //解析system.sys、DB_HOST文件生成默认集合关系以及默认连线关系
            List<String> sameSetHostIpList = null;
            String prjhome = System.getenv("PRJHOME");
            if (!StringUtils.isEmpty(prjhome)) {
                File iniFile = new File(prjhome + DOCKER_INI_PATH_SUFFIX);
                File sysFile = new File(prjhome + SYSTEM_SYS_PATH_SUFFIX);
                if (iniFile.exists() && iniFile.isFile() && sysFile.exists() && sysFile.isFile()) {
                    Ini ini = new Ini(iniFile);
                    Preferences prefs = new IniPreferences(ini);
                    String uscadaHostName = prefs.node("foundation config").get("HOSTNAME", null);
                    if (!StringUtils.isEmpty(uscadaHostName)) {
                        GET_SAME_SET_LIST[2] = GET_SAME_SET_LIST[2].replaceFirst("USCADAHOSTNAME", uscadaHostName.toLowerCase());
                        try{
                            sameSetHostIpList = RuntimeUtil.execForLines(GET_SAME_SET_LIST);
                        } catch (Exception e){
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
            String dbHost = System.getenv("DB_HOST");
            if (!StringUtils.isEmpty(dbHost) && !dbHost.equals(hostIp)) {
                if (sameSetHostIpList == null) {
                    sameSetHostIpList = new LinkedList<>();
                } else {
                    //放到这做校验，可以减少判断逻辑语句
                    // 通过第一个元素的形式判断是否正确获取到主机IP
                    String firstIp = sameSetHostIpList.get(0);
                    if (!firstIp.matches(IP_PATTERN)) {
                        log.error("Ip format of system.sys is not correct: " + firstIp);
                        sameSetHostIpList.clear();
                    }
                }
                // DB_HOST的IP格式不做校验
                sameSetHostIpList.add(dbHost);
            }
            HashSet<String> sameSetHostIpSet = null;
            List<TopoRelaInfo> relaInfoList = null;
            int sameSetSize = 0;
            if (!CollectionUtils.isEmpty(sameSetHostIpList)) {
                sameSetHostIpSet = new HashSet<>(sameSetHostIpList);
                sameSetHostIpList.clear();
                sameSetHostIpList.addAll(sameSetHostIpSet);
                sameSetSize = sameSetHostIpList.size();
                topoInfo.setSameSetHostIpList(sameSetHostIpList);
                relaInfoList = sameSetHostIpList.parallelStream().map(sameSet -> {
                    TopoRelaInfo topoRelaInfo = new TopoRelaInfo();
                    topoRelaInfo.setHostIpTarget(sameSet);
                    return topoRelaInfo;
                }).collect(Collectors.toList());
            }

            //解析ied_info表生成默认单向连线关系
            List<Integer> iedHostIpList = null;
            try {
                iedHostIpList = scadaMapper.getDistinctHostIp();
            } catch (Exception e) {
                log.info("Perhaps there is no datasource can be created by this host: ", e);
            }
            if (!CollectionUtils.isEmpty(iedHostIpList)) {
                HashSet<String> relaInfoSet = new HashSet<>(sameSetSize + iedHostIpList.size());
                if (sameSetSize > 0) {
                    relaInfoSet.addAll(sameSetHostIpSet);
                } else {
                    relaInfoList = new LinkedList<>();
                }
                for (int iedHostIp : iedHostIpList) {
                    String formatIp = convertIp(iedHostIp);
                    if (relaInfoSet.add(formatIp)) {
                        TopoRelaInfo topoRelaInfo = new TopoRelaInfo();
                        topoRelaInfo.setHostIpTarget(formatIp);
                        relaInfoList.add(topoRelaInfo);
                    }
                }
            }
            topoInfo.setRelaInfoList(relaInfoList);

            //更新至远程
            reqBody.setData(Collections.singletonList(topoInfo));
            pushInitMonitorObj(reqBody);
        } catch (Exception e) {
            log.error("init monitor task={} failed.", getTaskInfo());
            log.error(e.getMessage(), e);
        }
    }

    private String convertIp(int val) {
        long unsignVal = Integer.toUnsignedLong(val);
        return String.format("%d.%d.%d.%d", unsignVal / 256 / 256 / 256, unsignVal / 256 / 256 % 256, unsignVal / 256 % 256, unsignVal % 256);
    }

    private static final String[] GET_GATEWAY_LIST = {"sh", "-c", "route -n | awk 'NR>2{if($1==\"0.0.0.0\")print$2}'"};

    private static final String[] GET_PING_MILLIS = {"sh", "-c", "ping x.x.x.x -c 1 -W 1 | awk 'NR==2{if($0~\"bytes from\")print$0}' | grep -o \"time=.* ms\" | awk '{print substr($0,6,length-8)}'"};

    private static final String[] GET_SAME_SET_LIST = {"sh", "-c", "cat $PRJHOME/config/scada_sys/system.sys | awk 'NR>1{if(tolower($4)!=\"USCADAHOSTNAME\")print$5}'"};

    private static final String IP_PATTERN = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";

    private static final String DOCKER_INI_PATH_SUFFIX = "/config/docker_start_envision.ini";

    private static final String SYSTEM_SYS_PATH_SUFFIX = "/config/scada_sys/system.sys";


    private List<String> gatewayIpList;

    @Autowired
    private ScadaMapper scadaMapper;
}
