package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.HostStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.HostCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.HostFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HostFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:05
 */
public class HostFdbDao extends HostCommDao {

    @Autowired
    private HostFWMapper hostFWMapper;

    @Override
    public void update(HostStat sample) {
        int updNum = hostFWMapper.update_update_first(sample);
        if (updNum <= 0) {
            hostFWMapper.update_insert_iffail(sample);
        }
    }

    @Override
    public int offlineHost(List<String> needOfflineHostList) {
        return hostFWMapper.offlineHost(needOfflineHostList);
    }

    @Override
    public void insertHostAlarmBatch(List<String> needOfflineHostList, short alarmType, String alarmContent) {
        hostFWMapper.insertHostAlarmBatch(needOfflineHostList, alarmType, alarmContent);
    }

    @Override
    public List<String> qryHostAlarmObj(List<String> hostIpList) {
        return hostFWMapper.qryHostAlarmObj(hostIpList);
    }
}
