package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.HostStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HostMysqlMapper {

    void update(@Param("host") HostStat sample);

    int offlineHost(List<String> needOfflineHostList);

    void insertHostAlarmBatch(@Param("list") List<String> needOfflineHostList, @Param("alarmType") short alarmType, @Param("alarmContent") String alarmContent);

    List<String> qryHostAlarmObj(@Param("hostIpList") List<String> hostIpList);


}
