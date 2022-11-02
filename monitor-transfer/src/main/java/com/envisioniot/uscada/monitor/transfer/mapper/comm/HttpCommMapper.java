package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import com.envisioniot.uscada.monitor.common.entity.HttpInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HttpCommMapper {

    List<HttpInfo> getMonitorObj(@Param("host_ip") String hostIp);
}
