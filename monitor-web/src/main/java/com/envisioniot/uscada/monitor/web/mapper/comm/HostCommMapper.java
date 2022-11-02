package com.envisioniot.uscada.monitor.web.mapper.comm;

import com.envisioniot.uscada.monitor.web.entity.HostPropResp;
import com.envisioniot.uscada.monitor.web.entity.HostTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HostCommMapper {

    /**
     * 获取主机监控的时序数据
     */
    List<HostTable> getTimeSeriesList();

    HostTable getInfo(@Param("host_ip") String hostIp);

    List<HostPropResp> getHostProps(@Param("match_flag")String matchFlag);
}
