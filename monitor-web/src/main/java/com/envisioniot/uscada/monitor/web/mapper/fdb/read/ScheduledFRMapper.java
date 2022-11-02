package com.envisioniot.uscada.monitor.web.mapper.fdb.read;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ScheduledFRMapper
 *
 * @author yangkang
 * @date 2021/1/19
 */
public interface ScheduledFRMapper {

    List<String> qryNeedOfflineHost();

    List<String> qryHostAlarmObj(@Param("hostIpList") List<String> hostIpList);
}
