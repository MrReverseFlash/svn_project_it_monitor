package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import com.envisioniot.uscada.monitor.common.entity.AppObj;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppCommMapper {

    List<AppObj> getMonitorApps(@Param("host_ip") String hostIp);

}
