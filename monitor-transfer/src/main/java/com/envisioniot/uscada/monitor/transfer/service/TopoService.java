package com.envisioniot.uscada.monitor.transfer.service;

import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.TopoDao;
import com.envisioniot.uscada.monitor.transfer.exception.DataProcessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * TopoService
 *
 * @author yangkang
 * @date 2021/3/1
 */
@Slf4j
@Service
public class TopoService {

    @Autowired
    private TopoDao topoDao;

    @Transactional(rollbackFor = Exception.class)
    public void saveSample(CommStat<TopoInfo> sample, Boolean isInit) {
        String hostIp = sample.getHostIp();
        try {
            List<TopoInfo> sampleData = sample.getData();
            TopoInfo topoInfo = sampleData.get(0);
            List<GatewayInfo> gatewayInfoList = topoInfo.getGatewayInfoList();
            List<TopoRelaInfo> relaInfoList = topoInfo.getRelaInfoList();
            List<String> sameSetHostIpList = topoInfo.getSameSetHostIpList();
            if (isInit) {
                //1.插入拓扑单元与连线关系
                topoDao.insHost(hostIp);
                if (!CollectionUtils.isEmpty(relaInfoList)) {
                    topoDao.insAssociatedHost(relaInfoList);
                    topoDao.delNotExtDefRelaByHost(hostIp, relaInfoList);
                    topoDao.insDefRelaByHost(hostIp, relaInfoList);
                }
                //2.插入网关列表
                if (!CollectionUtils.isEmpty(gatewayInfoList)) {
                    topoDao.delNotExtGwByHost(hostIp, gatewayInfoList);
                    topoDao.insGwList(hostIp, gatewayInfoList);
                }
                //3.生成默认集合关系
                if (!CollectionUtils.isEmpty(sameSetHostIpList)) {
                    String hostSetId = topoDao.qrySetIdByHost(hostIp);
                    if (hostSetId != null) {
                        topoDao.updSetId(hostSetId, sameSetHostIpList);
                    } else {
                        String minSetId = topoDao.qryMinSetId(sameSetHostIpList);
                        sameSetHostIpList.add(hostIp);
                        if (minSetId != null) {
                            topoDao.updSetId(minSetId, sameSetHostIpList);
                        } else {
                            TopoParam topoParam = new TopoParam();
                            topoParam.setId(UUID.randomUUID().toString());
                            topoParam.setNum((short) 0);
                            topoParam.setName("默认生成");
                            topoParam.setType((short) 2);
                            topoDao.insTopoParam(topoParam);
                            topoDao.updSetId(topoParam.getId(), sameSetHostIpList);
                        }
                    }
                }
            } else {
                //1.更新网关状态
                if (!CollectionUtils.isEmpty(gatewayInfoList)) {
                    topoDao.updGwList(hostIp, gatewayInfoList);
                }
                //2.更新连线状态
                if (!CollectionUtils.isEmpty(relaInfoList)) {
                    topoDao.updRelaList(hostIp, relaInfoList);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} app sample data save fail.", hostIp));
        }
    }

    public List<String> getMonitorRelaHosts(String hostIp) {
        return topoDao.qryRelaByHost(hostIp);
    }
}
