package com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.web.dao.rdb.ScheduledCommDao;
import com.envisioniot.uscada.monitor.web.mapper.fdb.read.ScheduledFRMapper;
import com.envisioniot.uscada.monitor.web.mapper.fdb.write.ScheduledFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: ScheduledFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 15:42
 */
public class ScheduledFdbDao extends ScheduledCommDao {

    @Autowired
    private ScheduledFRMapper scheduledFRMapper;

    @Autowired
    private ScheduledFWMapper scheduledFWMapper;

    @Override
    public List<String> qryNeedOfflineHost() {
        return scheduledFRMapper.qryNeedOfflineHost();
    }

    @Override
    public int offlineHost(List<String> needOfflineHostList) {
        return scheduledFWMapper.offlineHost(needOfflineHostList);
    }

    @Override
    public void insertHostAlarmBatch(List<String> needOfflineHostList, short alarmType, String alarmContent) {
        scheduledFWMapper.insertHostAlarmBatch(needOfflineHostList, alarmType, alarmContent);
    }

    @Override
    public int offlineApp(List<String> onlineHostList) {
        return scheduledFWMapper.offlineApp(onlineHostList);
    }

    @Override
    public int offlineDB(List<String> onlineHostList) {
        return scheduledFWMapper.offlineDB(onlineHostList);
    }

    @Override
    public int offlineDocker(List<String> onlineHostList) {
        return scheduledFWMapper.offlineDocker(onlineHostList);
    }

    @Override
    public List<String> qryHostAlarmObj(List<String> hostIpList) {
        return scheduledFRMapper.qryHostAlarmObj(hostIpList);
    }
}
