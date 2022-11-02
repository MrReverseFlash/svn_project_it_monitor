package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import com.envisioniot.uscada.monitor.common.entity.IedInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IedCommMapper {

    List<IedInfo> getMonitorObj(@Param("host_ip") String hostIp);
}
