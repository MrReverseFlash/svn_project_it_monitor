package com.envisioniot.uscada.monitor.transfer.service;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.common.entity.IedInfo;
import com.envisioniot.uscada.monitor.common.entity.IedStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.IedDao;
import com.envisioniot.uscada.monitor.transfer.dao.tsdb.IedInfluxDao;
import com.envisioniot.uscada.monitor.transfer.exception.DataProcessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-28
 */
@Service
@Slf4j
public class IedService {

    @Autowired
    private IedInfluxDao iedInfluxDao;

    @Autowired
    private IedDao iedDao;

    public void saveSample(CommStat<IedStat> sample, Boolean isInit) {
        String hostIp = sample.getHostIp();
        try {
            List<IedStat> sampleData = sample.getData();
            if (CollectionUtil.isEmpty(sampleData)) {
                log.warn("host ip = {} ied sample is empty.", hostIp);
                return;
            }
            if (isInit) {
                iedDao.insertBatch(hostIp, sampleData);
            } else {
                // 更新HDB中的状态和监控值
                iedDao.batchForUpdate(hostIp, sampleData);
                // 插入采样信息到TSDB
                iedInfluxDao.insertHis(hostIp, sampleData);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} ied sample data save fail.", hostIp));
        }
    }

    public List<IedInfo> getMonitorIed(String hostIp) {
        return iedDao.getMonitorObj(hostIp);
    }
}
