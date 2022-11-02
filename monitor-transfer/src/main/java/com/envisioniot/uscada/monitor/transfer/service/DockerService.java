package com.envisioniot.uscada.monitor.transfer.service;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.common.entity.ContainerStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DockerDao;
import com.envisioniot.uscada.monitor.transfer.dao.tsdb.DockerInfluxDao;
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
public class DockerService {

    @Autowired
    private DockerInfluxDao dockerInfluxDao;

    @Autowired
    private DockerDao dockerDao;

    public void saveSample(CommStat<ContainerStat> sample, Boolean isInit) {
        String hostIp = sample.getHostIp();
        try {
            List<ContainerStat> sampleData = sample.getData();
            if (CollectionUtil.isEmpty(sampleData)) {
                log.warn("host ip = {} docker container sample is empty.", hostIp);
                return;
            }
            if (isInit) {
                dockerDao.insertBatch(hostIp, sampleData);
            } else {
                // 更新HDB中的状态和监控值
                dockerDao.batchForUpdate(hostIp, sampleData);
                // 插入采样信息到TSDB
                dockerInfluxDao.insertHis(hostIp, sampleData);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} app sample data save fail.", hostIp));
        }
    }

    public List<String> getMonitorContainerIds(String hostIp) {
        return dockerDao.getMonitorContainerIds(hostIp);
    }
}
