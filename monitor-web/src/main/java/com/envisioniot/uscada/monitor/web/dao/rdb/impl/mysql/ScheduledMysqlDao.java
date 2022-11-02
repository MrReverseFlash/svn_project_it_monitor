package com.envisioniot.uscada.monitor.web.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.web.dao.rdb.ScheduledCommDao;
import com.envisioniot.uscada.monitor.web.mapper.mysql.ScheduledMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: ScheduledMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 15:26
 */
public class ScheduledMysqlDao extends ScheduledCommDao {

    @Autowired
    private ScheduledMysqlMapper scheduledMysqlMapper;

    @Override
    public List<String> qryNeedOfflineHost() {
        return scheduledMysqlMapper.qryNeedOfflineHost();
    }

    @Override
    public int offlineHost(List<String> needOfflineHostList) {
        return scheduledMysqlMapper.offlineHost(needOfflineHostList);
    }

    @Override
    public void insertHostAlarmBatch(List<String> needOfflineHostList, short alarmType, String alarmContent) {
        scheduledMysqlMapper.insertHostAlarmBatch(needOfflineHostList, alarmType, alarmContent);
    }

    @Override
    public int offlineApp(List<String> onlineHostList) {
        return scheduledMysqlMapper.offlineApp(onlineHostList);
    }

    @Override
    public int offlineDB(List<String> onlineHostList) {
        return scheduledMysqlMapper.offlineDB(onlineHostList);
    }

    @Override
    public int offlineDocker(List<String> onlineHostList) {
        return scheduledMysqlMapper.offlineDocker(onlineHostList);
    }

    @Override
    public List<String> qryHostAlarmObj(List<String> hostIpList) {
        return scheduledMysqlMapper.qryHostAlarmObj(hostIpList);
    }
}
