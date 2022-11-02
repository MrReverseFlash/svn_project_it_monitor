package com.envisioniot.uscada.monitor.transfer.service;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.common.entity.PortInfo;
import com.envisioniot.uscada.monitor.common.entity.PortStatSample;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.PortDao;
import com.envisioniot.uscada.monitor.transfer.dao.tsdb.PortInfluxDao;
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
public class PortService {
    @Autowired
    private PortInfluxDao portInfluxDao;

    @Autowired
    private PortDao portDao;

    public void saveSample(CommStat<PortStatSample> sample, boolean isInit) {
        String hostIp = sample.getHostIp();
        try {
            List<PortStatSample> sampleData = sample.getData();
            if (CollectionUtil.isEmpty(sampleData)) {
                log.warn("host ip = {} port sample is empty.", hostIp);
                return;
            }
            if(isInit){
                portDao.insertBatch(hostIp, sampleData);
            }else{
                // 更新HDB中的状态和监控值
                portDao.batchForUpdate(hostIp, sampleData);
                // 插入采样信息到TSDB
                portInfluxDao.insertHis(hostIp, sampleData);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} port sample data save fail.", hostIp));
        }
    }

    public List<PortInfo> getMonitorPort(String hostIp) {
        return portDao.getMonitorObj(hostIp);
    }
}
