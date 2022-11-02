package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.HostStat;

import java.util.List;

/**
 * <p>Title: HostDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:02
 */
public interface HostDao {

    void update(HostStat sample);

    String qryHostNameByIp(String hostIp);

    int offlineHost(List<String> needOfflineHostList);

    void insertHostAlarmBatch(List<String> needOfflineHostList, short alarmType, String alarmContent);


    List<String> queryAllOnlineHostIps(String matchFlag);

    List<String> qryHostAlarmObj(List<String> hostIpList);
}
