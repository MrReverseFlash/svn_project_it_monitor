package com.envisioniot.uscada.monitor.web.mapper.fdb.write;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ScheduledFWMapper
 *
 * @author yangkang
 * @date 2021/1/19
 */
public interface ScheduledFWMapper {

    int offlineHost(List<String> needOfflineHostList);

    void insertHostAlarmBatch(@Param("list") List<String> needOfflineHostList, @Param("alarmType") short alarmType, @Param("alarmContent") String alarmContent);

    int offlineApp(List<String> onlineHostList);

    int offlineDB(List<String> onlineHostList);

    int offlineDocker(List<String> onlineHostList);
}
