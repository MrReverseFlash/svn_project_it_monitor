package com.envisioniot.uscada.monitor.web.mapper.comm;

import com.envisioniot.uscada.monitor.web.entity.DockerInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DockerCommMapper {

    List<DockerInfo> qryAll();

    List<DockerInfo> query(@Param("host_ip") String hostIp);

}
