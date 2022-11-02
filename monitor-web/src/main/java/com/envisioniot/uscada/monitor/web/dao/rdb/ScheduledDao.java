package com.envisioniot.uscada.monitor.web.dao.rdb;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: ScheduledDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 15:24
 */
public interface ScheduledDao {

    List<String> qryNeedOfflineHost();

    int offlineHost(List<String> needOfflineHostList);

    void insertHostAlarmBatch(List<String> needOfflineHostList, short alarmType, String alarmContent);

    int offlineApp(List<String> onlineHostList);

    int offlineDB(List<String> onlineHostList);

    int offlineDocker(List<String> onlineHostList);

    List<String> qryHostAlarmObj(List<String> hostIpList);

}
