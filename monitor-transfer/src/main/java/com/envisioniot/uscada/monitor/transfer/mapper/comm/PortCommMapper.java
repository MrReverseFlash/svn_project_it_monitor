package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import com.envisioniot.uscada.monitor.common.entity.PortInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PortCommMapper {

    List<PortInfo> getMonitorObj(@Param("host_ip") String hostIp);
}
