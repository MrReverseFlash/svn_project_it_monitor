package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DockerCommMapper {

    List<String> getMonitorContainerIds(@Param("host_ip") String hostIp);
}