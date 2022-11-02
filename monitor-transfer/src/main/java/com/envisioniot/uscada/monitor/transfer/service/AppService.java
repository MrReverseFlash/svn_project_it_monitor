package com.envisioniot.uscada.monitor.transfer.service;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.common.entity.AppObj;
import com.envisioniot.uscada.monitor.common.entity.AppStat;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.AppDao;
import com.envisioniot.uscada.monitor.transfer.dao.tsdb.AppInfluxDao;
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
public class AppService {

    @Autowired
    private AppInfluxDao appInfluxDao;

    @Autowired
    private AppDao appDao;

    public void saveSample(CommStat<AppStat> sample, boolean isInit) {
        String hostIp = sample.getHostIp();
        try {
            List<AppStat> sampleData = sample.getData();
            if (CollectionUtil.isEmpty(sampleData)) {
                log.warn("host ip = {} app sample is empty.", hostIp);
                return;
            }
            if(isInit){
                appDao.insert(hostIp, sampleData);
            }else{
                appDao.batchForUpdate(hostIp, sampleData);
                // 插入采样信息到TSDB
                appInfluxDao.insertHis(hostIp, sampleData);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} app sample data save fail.", hostIp));
        }
    }

    public List<AppObj> getMonitorObj(String hostIp) {
        return appDao.getMonitorApps(hostIp);
    }
}
