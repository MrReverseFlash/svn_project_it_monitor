package com.envisioniot.uscada.monitor.transfer.service;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.common.entity.HttpInfo;
import com.envisioniot.uscada.monitor.common.entity.HttpStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.HttpDao;
import com.envisioniot.uscada.monitor.transfer.dao.tsdb.HttpInfluxDao;
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
public class HttpService {
    @Autowired
    private HttpInfluxDao httpInfluxDao;

    @Autowired
    private HttpDao httpDao;

    public void saveSample(CommStat<HttpStat> sample, boolean isInit) {
        String hostIp = sample.getHostIp();
        try {
            List<HttpStat> sampleData = sample.getData();
            if (CollectionUtil.isEmpty(sampleData)) {
                log.warn("host ip = {} http sample is empty.", hostIp);
                return;
            }
            if (isInit) {
                httpDao.insertBatch(hostIp, sampleData);
            } else {
                // 更新HDB中的状态和监控值
                httpDao.batchForUpdate(hostIp, sampleData);
                // 插入采样信息到TSDB
                httpInfluxDao.insertHis(hostIp, sampleData);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} http sample data save fail.", hostIp));
        }
    }

    public List<HttpInfo> getMonitorHttp(String hostIp) {
        return httpDao.getMonitorObj(hostIp);
    }
}
