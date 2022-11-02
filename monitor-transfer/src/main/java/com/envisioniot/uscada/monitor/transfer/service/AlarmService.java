package com.envisioniot.uscada.monitor.transfer.service;

import com.alibaba.fastjson.JSONObject;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.transfer.config.AlarmProp;
import com.envisioniot.uscada.monitor.transfer.config.AlarmProperties;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.AlarmDao;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DbInfoDao;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.HostDao;
import com.envisioniot.uscada.monitor.transfer.util.UscadaClient;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.*;
import static com.envisioniot.uscada.monitor.transfer.util.CommConstants.*;

/**
 * AlarmService
 *
 * @author yangkang
 * @date 2021/8/20
 */
@Slf4j
@Service
public class AlarmService {

    @Autowired
    private AlarmProperties alarmProperties;

    @Autowired(required = false)
    private Cache<String, Object> cpuCache;

    @Autowired(required = false)
    private Cache<String, Object> hostCache;

    @Autowired(required = false)
    private Cache<String, Object> memCache;

    @Autowired(required = false)
    private Cache<String, Object> diskCache;

    @Value("${uscadaIp:127.0.0.1}")
    private String uscadaIp;

    @Autowired
    private AlarmDao alarmDao;

    @Autowired
    private HostDao hostDao;

    @Autowired
    private DbInfoDao dbInfoDao;

    private ConcurrentHashMap<String, Boolean> dbStatusMap = new ConcurrentHashMap();

    @Async("taskExecutor")
    public void geneCpuAlarm(HostStat sample) {
        try {
            //先缓存
            //缓存KEY_1格式：IP@dd-HH:mm，如：127.0.0.1@30-11:30
            //缓存VALUE_1格式：CPU使用率值
            //缓存KEY_2格式：IP，如：127.0.0.1
            //缓存VALUE_2格式：最后一次告警的边界时间时间戳
            String hostIp = sample.getHostIp();
            String occurTime = sample.getOccurTime();
            LocalDateTime localDateTime = LocalDateTime.parse(occurTime, TIME_FORMATTER);
            String dayHourMin = localDateTime.toString(CACHE_TIMEFORMAT);
            CpuStat cpuSample = sample.getCpuSample();
            double cpuPer = cpuSample.getSystem() + cpuSample.getUser();
            cpuCache.put(hostIp + dayHourMin, cpuPer);
            //后判断是否产生告警
            AlarmProp cpuProp = alarmProperties.getCpu();
            short num = 1;
            for (short i = 1; i <= cpuProp.getSampleDuration(); i++) {
                Object hisCpuPer = cpuCache.getIfPresent(hostIp + localDateTime.minusMinutes(i).toString(CACHE_TIMEFORMAT));
                if (hisCpuPer != null) {
                    cpuPer += (double) hisCpuPer;
                    num++;
                }
            }
            double avg = cpuPer / num;
            short halfMin = (short) (cpuProp.getSampleDuration() / 2);
            Object lastAlarmTimestamp = cpuCache.getIfPresent(hostIp);
            if (avg > cpuProp.getAlarmThreshold() && num > halfMin) {
                long alarmTimestamp = localDateTime.toDateTime(DateTimeZone.UTC).getMillis();
                cpuCache.put(hostIp, alarmTimestamp);
                if (lastAlarmTimestamp == null) {
                    AlarmHostInfo alarmHostInfo = new AlarmHostInfo();
                    alarmHostInfo.setHostIp(hostIp);
                    alarmHostInfo.setOccurTime(occurTime);
                    alarmHostInfo.setAlarmType(ALARMTYPE_CPU);
                    alarmHostInfo.setAlarmContent(cpuProp.getAlarmContent());
                    alarmDao.insertHostAlarm(alarmHostInfo);
                    //向scada推送新告警
                    String warnObj = "[" + hostIp + "]";
                    String hostName = hostDao.qryHostNameByIp(hostIp);
                    if (hostName != null) {
                        warnObj = hostName + warnObj;
                    }
                    UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, warnObj, cpuProp.getAlarmContent());
                    List<UscadaAlarm> alarmList = Collections.singletonList(uscadaAlarm);
                    JSONObject reqBody = new JSONObject(2);
                    reqBody.put("alarm_list", alarmList);
                    JSONObject respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                    if (!"10000".equals(respJson.getString("code"))) {
                        log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                    }
                } else {
                    short alarmInterval = cpuProp.getAlarmInterval();
                    long alarmIntervalTimestamp = (long) lastAlarmTimestamp + alarmInterval * ONEMINUTE_MILLIS;
                    if (alarmTimestamp >= alarmIntervalTimestamp) {
                        AlarmHostInfo alarmHostInfo = new AlarmHostInfo();
                        alarmHostInfo.setHostIp(hostIp);
                        alarmHostInfo.setOccurTime(occurTime);
                        alarmHostInfo.setAlarmType(ALARMTYPE_CPU);
                        alarmHostInfo.setAlarmContent(cpuProp.getAlarmContent());
                        alarmDao.insertHostAlarm(alarmHostInfo);
                        //向scada推送新告警
                        String warnObj = "[" + hostIp + "]";
                        String hostName = hostDao.qryHostNameByIp(hostIp);
                        if (hostName != null) {
                            warnObj = hostName + warnObj;
                        }
                        UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, warnObj, cpuProp.getAlarmContent());
                        List<UscadaAlarm> alarmList = Collections.singletonList(uscadaAlarm);
                        JSONObject reqBody = new JSONObject(2);
                        reqBody.put("alarm_list", alarmList);
                        JSONObject respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                        if (!"10000".equals(respJson.getString("code"))) {
                            log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async("taskExecutor")
    public void geneMemAlarm(HostStat sample) {
        try {
            //先缓存
            //缓存KEY_1格式：IP@dd-HH:mm，如：127.0.0.1@30-11:30
            //缓存VALUE_1格式：内存使用率值
            //缓存KEY_2格式：IP，如：127.0.0.1
            //缓存VALUE_2格式：最后一次告警的边界时间时间戳
            String hostIp = sample.getHostIp();
            String occurTime = sample.getOccurTime();
            LocalDateTime localDateTime = LocalDateTime.parse(occurTime, TIME_FORMATTER);
            MemStat memSample = sample.getMemSample();
            double memPer = memSample.getUsePer();
            memCache.put(hostIp + localDateTime.toString(CACHE_TIMEFORMAT), memPer);
            //后判断是否产生告警
            AlarmProp memProp = alarmProperties.getMem();
            short num = 1;
            for (short i = 1; i <= memProp.getSampleDuration(); i++) {
                Object hisMemPer = memCache.getIfPresent(hostIp + localDateTime.minusMinutes(i).toString(CACHE_TIMEFORMAT));
                if (hisMemPer != null) {
                    memPer += (double) hisMemPer;
                    num++;
                }
            }
            double avg = memPer / num;
            short halfMin = (short) (memProp.getSampleDuration() / 2);
            Object lastAlarmTimestamp = memCache.getIfPresent(hostIp);
            if (avg > memProp.getAlarmThreshold() && num > halfMin) {
                long alarmTimestamp = localDateTime.toDateTime(DateTimeZone.UTC).getMillis();
                memCache.put(hostIp, alarmTimestamp);
                if (lastAlarmTimestamp == null) {
                    AlarmHostInfo alarmHostInfo = new AlarmHostInfo();
                    alarmHostInfo.setHostIp(hostIp);
                    alarmHostInfo.setOccurTime(occurTime);
                    alarmHostInfo.setAlarmType(ALARMTYPE_MEM);
                    alarmHostInfo.setAlarmContent(memProp.getAlarmContent());
                    alarmDao.insertHostAlarm(alarmHostInfo);
                    //向scada推送新告警
                    String warnObj = "[" + hostIp + "]";
                    String hostName = hostDao.qryHostNameByIp(hostIp);
                    if (hostName != null) {
                        warnObj = hostName + warnObj;
                    }
                    UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, warnObj, memProp.getAlarmContent());
                    List<UscadaAlarm> alarmList = Collections.singletonList(uscadaAlarm);
                    JSONObject reqBody = new JSONObject(2);
                    reqBody.put("alarm_list", alarmList);
                    JSONObject respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                    if (!"10000".equals(respJson.getString("code"))) {
                        log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                    }
                } else {
                    short alarmInterval = memProp.getAlarmInterval();
                    long alarmIntervalTimestamp = (long) lastAlarmTimestamp + alarmInterval * ONEMINUTE_MILLIS;
                    if (alarmTimestamp >= alarmIntervalTimestamp) {
                        AlarmHostInfo alarmHostInfo = new AlarmHostInfo();
                        alarmHostInfo.setHostIp(hostIp);
                        alarmHostInfo.setOccurTime(occurTime);
                        alarmHostInfo.setAlarmType(ALARMTYPE_MEM);
                        alarmHostInfo.setAlarmContent(memProp.getAlarmContent());
                        alarmDao.insertHostAlarm(alarmHostInfo);
                        //向scada推送新告警
                        String warnObj = "[" + hostIp + "]";
                        String hostName = hostDao.qryHostNameByIp(hostIp);
                        if (hostName != null) {
                            warnObj = hostName + warnObj;
                        }
                        UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, warnObj, memProp.getAlarmContent());
                        List<UscadaAlarm> alarmList = Collections.singletonList(uscadaAlarm);
                        JSONObject reqBody = new JSONObject(2);
                        reqBody.put("alarm_list", alarmList);
                        JSONObject respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                        if (!"10000".equals(respJson.getString("code"))) {
                            log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async("taskExecutor")
    public void geneDiskAlarm(HostStat sample) {
        try {
            //先缓存
            //缓存KEY_1格式：IP+MOUNTPOINT+@dd-HH:mm，如：127.0.0.1/@30-11:30
            //缓存VALUE_1格式：硬盘使用率值
            //缓存KEY_2格式：IP+MOUNTPOINT，如：127.0.0.1/home/windos
            //缓存VALUE_2格式：最后一次告警的边界时间时间戳
            String hostIp = sample.getHostIp();
            String occurTime = sample.getOccurTime();
            LocalDateTime localDateTime = LocalDateTime.parse(occurTime, TIME_FORMATTER);
            List<DiskStat> diskList = sample.getDiskList();
            for (DiskStat diskSample : diskList) {
                String mountPoint = diskSample.getDirName();
                double diskPer = diskSample.getUsePer();
                diskCache.put(hostIp + mountPoint + localDateTime.toString(CACHE_TIMEFORMAT), diskPer);
                //后判断是否产生告警
                AlarmProp diskProp = alarmProperties.getDisk();
                short num = 1;
                for (short i = 1; i <= diskProp.getSampleDuration(); i++) {
                    Object hisDiskPer = diskCache.getIfPresent(hostIp + mountPoint + localDateTime.minusMinutes(i).toString(CACHE_TIMEFORMAT));
                    if (hisDiskPer != null) {
                        diskPer += (double) hisDiskPer;
                        num++;
                    }
                }
                double avg = diskPer / num;
                short halfMin = (short) (diskProp.getSampleDuration() / 2);
                Object lastAlarmTimestamp = diskCache.getIfPresent(hostIp + mountPoint);
                if (avg > diskProp.getAlarmThreshold() && num > halfMin) {
                    long alarmTimestamp = localDateTime.toDateTime(DateTimeZone.UTC).getMillis();
                    diskCache.put(hostIp + mountPoint, alarmTimestamp);
                    if (lastAlarmTimestamp == null) {
                        AlarmHostInfo alarmHostInfo = new AlarmHostInfo();
                        alarmHostInfo.setHostIp(hostIp);
                        alarmHostInfo.setOccurTime(occurTime);
                        alarmHostInfo.setAlarmType(ALARMTYPE_DISK);
                        alarmHostInfo.setAlarmContent(diskProp.getAlarmContent().replaceFirst("arg1", mountPoint));
                        alarmDao.insertHostAlarm(alarmHostInfo);
                        //向scada推送新告警
                        String warnObj = "[" + hostIp + "]";
                        String hostName = hostDao.qryHostNameByIp(hostIp);
                        if (hostName != null) {
                            warnObj = hostName + warnObj;
                        }
                        UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, warnObj, diskProp.getAlarmContent());
                        List<UscadaAlarm> alarmList = Collections.singletonList(uscadaAlarm);
                        JSONObject reqBody = new JSONObject(2);
                        reqBody.put("alarm_list", alarmList);
                        JSONObject respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                        if (!"10000".equals(respJson.getString("code"))) {
                            log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                        }
                    } else {
                        short alarmInterval = diskProp.getAlarmInterval();
                        long alarmIntervalTimestamp = (long) lastAlarmTimestamp + alarmInterval * ONEMINUTE_MILLIS;
                        if (alarmTimestamp >= alarmIntervalTimestamp) {
                            AlarmHostInfo alarmHostInfo = new AlarmHostInfo();
                            alarmHostInfo.setHostIp(hostIp);
                            alarmHostInfo.setOccurTime(occurTime);
                            alarmHostInfo.setAlarmType(ALARMTYPE_DISK);
                            alarmHostInfo.setAlarmContent(diskProp.getAlarmContent().replaceFirst("arg1", mountPoint));
                            alarmDao.insertHostAlarm(alarmHostInfo);
                            //向scada推送新告警
                            String warnObj = "[" + hostIp + "]";
                            String hostName = hostDao.qryHostNameByIp(hostIp);
                            if (hostName != null) {
                                warnObj = hostName + warnObj;
                            }
                            UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, warnObj, diskProp.getAlarmContent());
                            List<UscadaAlarm> alarmList = Collections.singletonList(uscadaAlarm);
                            JSONObject reqBody = new JSONObject(2);
                            reqBody.put("alarm_list", alarmList);
                            JSONObject respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                            if (!"10000".equals(respJson.getString("code"))) {
                                log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async("taskExecutor")
    public void geneDbAlarm(CommStat<DbStat> sample) {
        try {
            for (DbStat dbStat : sample.getData()) {
                String dbId = dbStat.getId();
                Integer dbStatus = dbStat.getStatus();
                boolean nowStatus = (dbStatus != null && dbStatus == ONLINE_STATUS);
                if (dbId != null) {
                    Boolean lastStatus = dbStatusMap.put(dbId, nowStatus);
                    if (nowStatus == false && lastStatus != null && lastStatus == true) {
                        AlarmDbInfo alarmDbInfo = new AlarmDbInfo();
                        alarmDbInfo.setDbId(dbId);
                        alarmDbInfo.setOccurTime(dbStat.getOccurTime());
                        alarmDbInfo.setAlarmType(ALARMTYPE_DBSTAT);
                        alarmDbInfo.setAlarmContent(DBOFFLINE);
                        alarmDao.insertDbAlarm(alarmDbInfo);
                        //向scada推送新告警
                        String dbAlarmObj = dbInfoDao.qryDbAlarmObjById(dbId);
                        if (dbAlarmObj == null) {
                            dbAlarmObj = String.valueOf(dbId);
                        }
                        UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, dbAlarmObj, DBOFFLINE);
                        List<UscadaAlarm> alarmList = Collections.singletonList(uscadaAlarm);
                        JSONObject reqBody = new JSONObject(2);
                        reqBody.put("alarm_list", alarmList);
                        JSONObject respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                        if (!"10000".equals(respJson.getString("code"))) {
                            log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void putHostIpCache(HostStat sample) {
        hostCache.put(sample.getHostIp(), System.currentTimeMillis());
    }

    public void hostHealthCheck() {
        List<String> needOfflineHostList = new ArrayList<>();
        AlarmProp hostProp = alarmProperties.getHost();
        for (String hostIp : hostCache.asMap().keySet()) {
            if ((System.currentTimeMillis() - (Long) hostCache.getIfPresent(hostIp)) / 1000 / 60 > hostProp.getAlarmThreshold()) {
                needOfflineHostList.add(hostIp);
                hostCache.invalidate(hostIp);
            }
        }
        if (!CollectionUtils.isEmpty(needOfflineHostList)) {
            hostDao.offlineHost(needOfflineHostList);
            hostDao.insertHostAlarmBatch(needOfflineHostList, ALARMTYPE_HOSTSTAT, HOSTOFFLINE);
            //向scada推送新告警
            List<String> hostAlarmObjList = hostDao.qryHostAlarmObj(needOfflineHostList);
            if (hostAlarmObjList.size() > 100) {
                int i = 0;
                while (i < hostAlarmObjList.size() - 100) {
                    List<String> subAlarmList = hostAlarmObjList.subList(i, i + 100);
                    i += 100;
                    List<UscadaAlarm> alarmList = new ArrayList<>(100);
                    for (String hostAlarmObj : subAlarmList) {
                        UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, hostAlarmObj, HOSTOFFLINE);
                        alarmList.add(uscadaAlarm);
                    }
                    JSONObject reqBody = new JSONObject(2);
                    reqBody.put("alarm_list", alarmList);
                    JSONObject respJson = null;
                    try {
                        respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                        if (!"10000".equals(respJson.getString("code"))) {
                            log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                        }
                    } catch (Exception e) {
                        log.error("Pushing alarm to uscada failed!", e);
                    }
                }
                List<String> subAlarmList = hostAlarmObjList.subList(i, hostAlarmObjList.size());
                List<UscadaAlarm> alarmList = new ArrayList<>(subAlarmList.size());
                for (String hostAlarmObj : subAlarmList) {
                    UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, hostAlarmObj, HOSTOFFLINE);
                    alarmList.add(uscadaAlarm);
                }
                JSONObject reqBody = new JSONObject(2);
                reqBody.put("alarm_list", alarmList);
                JSONObject respJson = null;
                try {
                    respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                    if (!"10000".equals(respJson.getString("code"))) {
                        log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                    }
                } catch (Exception e) {
                    log.error("Pushing alarm to uscada failed!", e);
                }
            } else {
                List<UscadaAlarm> alarmList = new ArrayList<>(hostAlarmObjList.size());
                for (String hostAlarmObj : hostAlarmObjList) {
                    UscadaAlarm uscadaAlarm = new UscadaAlarm(WARN_TYPE_ITMONITOR, hostAlarmObj, HOSTOFFLINE);
                    alarmList.add(uscadaAlarm);
                }
                JSONObject reqBody = new JSONObject(2);
                reqBody.put("alarm_list", alarmList);
                JSONObject respJson = null;
                try {
                    respJson = UscadaClient.sendAlarm(uscadaIp, reqBody);
                    if (!"10000".equals(respJson.getString("code"))) {
                        log.error("Pushing alarm to uscada failed: " + respJson.getString("message"));
                    }
                } catch (Exception e) {
                    log.error("Pushing alarm to uscada failed!", e);
                }
            }
        }


    }

    @PostConstruct
    private void initHostIpsCache() {
        AlarmProp hostProp = alarmProperties.getHost();
        List<String> hostIps = hostDao.queryAllOnlineHostIps(hostProp.getMatchFlag());
        if (!CollectionUtils.isEmpty(hostIps)) {
            hostIps.forEach(ip -> hostCache.put(ip, System.currentTimeMillis()));
        }
    }
}
