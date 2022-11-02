package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HostCommMapper {

    String qryHostNameByIp(@Param("hostIp") String hostIp);

    List<String> queryAllOnlineHostIps(@Param("matchFlag")String matchFlag);
}
