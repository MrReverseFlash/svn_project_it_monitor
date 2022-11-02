package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import com.envisioniot.uscada.monitor.common.entity.DbStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbInfoCommMapper {

    List<DbStat> getMonitorDb(@Param("host_ip") String hostIp);
}
