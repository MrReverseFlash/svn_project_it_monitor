package com.envisioniot.uscada.monitor.web.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.web.dao.rdb.HostDao;
import com.envisioniot.uscada.monitor.web.dao.tsdb.HostInfluxDao;
import com.envisioniot.uscada.monitor.web.entity.*;
import com.envisioniot.uscada.monitor.web.exception.WebRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Service
@Slf4j
public class HostService {

    @Autowired
    private HostDao hostDao;

    @Autowired
    private HostInfluxDao hostInfluxDao;

    @Value("${host.matchFlag}")
    private String matchFlag;

    public List<HostTable> getTimeSeriesList() {
        try {
            List<HostTable> hostTableList = hostDao.getTimeSeriesList();
            if (!CollectionUtils.isEmpty(hostTableList)) {
                // status状态改为汉字
                hostTableList.forEach(hostTable -> {
                    hostTable.setHostMatch(matchFlag.equals(hostTable.getMatchFlag()));
                    if (!StringUtils.isEmpty(hostTable.getUscadaStatus())) {
                        String[] uscadaStatusArray = hostTable.getUscadaStatus().split(";");
                        StringBuilder newStatus = new StringBuilder();
                        for (String status : uscadaStatusArray) {
                            if (status.endsWith("1")) {
                                newStatus.append(status.split(":")[0]).append(":运行").append(";");
                            } else {
                                newStatus.append(status.split(":")[0]).append(":退出").append(";");
                            }
                        }
                        hostTable.setUscadaStatus(newStatus.substring(0, newStatus.length() - 1));
                    }
                });
            }
            return hostTableList;
        } catch (Exception e) {
            log.error("get time series from host table fail.");
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public void modifyLabel(LabelRequest request) {
        try {
            hostDao.modifyLabel(request.getId(), request.getLabel());
        } catch (Exception e) {
            log.error("modify label={} fail.", request);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public List<HostSampleResponse> getSample(String st, String et, String hostIp) {
        try {
            DateTime startTime = DateUtil.parse(st);
            DateTime endTime = DateUtil.parse(et);
            return hostInfluxDao.getHostSample(startTime.getTime(), endTime.getTime(), hostIp);
        } catch (Exception e) {
            log.error("get host={}, st={}, et={} sample fail.", hostIp, st, et);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public List<HostDetailResponse> getDetails(String st, String et, String hostIp) {
        try {
            DateTime startTime = DateUtil.parse(st);
            DateTime endTime = DateUtil.parse(et);
            return hostInfluxDao.getHostDetail(startTime.getTime(), endTime.getTime(), hostIp);
        } catch (Exception e) {
            log.error("get host={}, st={}, et={} details fail.", hostIp, st, et);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public List<HostDetailForDownload> getDetailsForDownload(String st, String et, String hostIp) {
        List<HostDetailResponse> responses = getDetails(st, et, hostIp);
        List<HostDetailForDownload> details = new ArrayList<>();
        if (!CollectionUtils.isEmpty(responses))
            responses.stream().forEach(res -> {
                HostDetailForDownload detail = new HostDetailForDownload();
                detail.setCpuIdle(res.getCpuSample().getIdle());
                detail.setCpuIowait(res.getCpuSample().getIowait());
                detail.setCpuUse(res.getCpuSample().getUser() + res.getCpuSample().getSystem());
                detail.setFiveLoad(res.getSysLoadSample().getFiveLoad());
                detail.setFifteenLoad(res.getSysLoadSample().getFifteenLoad());
                detail.setOneLoad(res.getSysLoadSample().getOneLoad());
                detail.setOccurTime(res.getOccurTime());
                detail.setRxpck(res.getNetSample().getRxpck());
                detail.setTxpck(res.getNetSample().getTxpck());
                detail.setTxbyt(res.getNetSample().getTxbyt());
                detail.setRxbyt(res.getNetSample().getRxbyt());
                details.add(detail);
            });
        return details;
    }


    public Map<String, List<NetSampleResponse>> getNetSample(String st, String et, String hostIp) {
        try {
            DateTime startTime = DateUtil.parse(st);
            DateTime endTime = DateUtil.parse(et);
            return hostInfluxDao.getNetSample(startTime.getTime(), endTime.getTime(), hostIp);
        } catch (Exception e) {
            log.error("get host={}, st={}, et={} net card sample fail.", hostIp, st, et);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public Map<String, List<DiskSampleResponse>> getDiskSample(String st, String et, String hostIp) {
        try {
            DateTime startTime = DateUtil.parse(st);
            DateTime endTime = DateUtil.parse(et);
            return hostInfluxDao.getDiskSample(startTime.getTime(), endTime.getTime(), hostIp);
        } catch (Exception e) {
            log.error("get host={}, st={}, et={} disk sample fail.", hostIp, st, et);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public HostTable getInfo(String hostIp) {
        try {
            return hostDao.getInfo(hostIp);
        } catch (Exception e) {
            log.error("get host={} info from host table fail.", hostIp);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    /**
     * @return 获取所有目前监控的主机列表配置信息
     */
    public List<HostPropResp> getPropList() {
        try {
            return hostDao.getHostProps(matchFlag);
        } catch (Exception e) {
            log.error("get time series from host table fail.");
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public List<AlarmResponse> getHostAlarm(String st, String et, String hostIp) {
        try {
            return hostDao.getHostAlarm(st, et, hostIp);
        } catch (Exception e) {
            log.error("get host={}, st={}, et={} host alarm fail.", hostIp, st, et);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public void delHost(LabelRequest request) {
        try {
            hostDao.delHost(request.getId());
        } catch (Exception e) {
            log.error("del host error", request);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }
}
