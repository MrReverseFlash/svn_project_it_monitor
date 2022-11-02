package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.HostStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.HostCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.HostMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.OFFLINE_STATUS;
import static com.envisioniot.uscada.monitor.common.util.CommConstants.ONLINE_STATUS;

/**
 * <p>Title: HostMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:04
 */
public class HostMysqlDao extends HostCommDao {

    @Autowired
    private HostMysqlMapper hostMysqlMapper;

    @Override
    public void update(HostStat sample) {
        hostMysqlMapper.update(sample);
    }
    @Override
    public int offlineHost(List<String> needOfflineHostList) {
        return hostMysqlMapper.offlineHost(needOfflineHostList);
    }

    @Override
    public void insertHostAlarmBatch(List<String> needOfflineHostList, short alarmType, String alarmContent) {
        hostMysqlMapper.insertHostAlarmBatch(needOfflineHostList, alarmType, alarmContent);
    }

    @Override
    public List<String> qryHostAlarmObj(List<String> hostIpList) {
        return hostMysqlMapper.qryHostAlarmObj(hostIpList);
    }


}
