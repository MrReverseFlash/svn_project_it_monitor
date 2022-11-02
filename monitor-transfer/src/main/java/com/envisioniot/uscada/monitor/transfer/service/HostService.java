package com.envisioniot.uscada.monitor.transfer.service;

import com.envisioniot.uscada.monitor.common.entity.HostStat;
import com.envisioniot.uscada.monitor.transfer.config.AlarmProperties;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.HostDao;
import com.envisioniot.uscada.monitor.transfer.dao.tsdb.HostInfluxDao;
import com.envisioniot.uscada.monitor.transfer.exception.DataProcessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hao.luo
 * @date 2020-12-25
 */
@Service
@Slf4j
public class HostService {

    @Autowired
    private HostInfluxDao hostInfluxDao;

    @Autowired
    private HostDao hostDao;

    @Autowired
    private AlarmProperties alarmProperties;

    public void saveSample(HostStat sample){
        try{
            sample.setMatchFlag(alarmProperties.getHost().getMatchFlag());
            // 更新HDB中的状态和监控值
            hostDao.update(sample);
            // 插入采样信息到TSDB
            hostInfluxDao.insertHis(sample);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new DataProcessException("host sample data save fail.");
        }
    }
}
