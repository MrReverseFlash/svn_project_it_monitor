package com.envisioniot.uscada.monitor.web.mapper.mysql;

import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HostMysqlMapper {

    void modifyLabel(@Param("id") Integer id,
                     @Param("label") String label);

    List<AlarmResponse> getHostAlarm(@Param("st") String st,
                                     @Param("et") String et,
                                     @Param("hostIp") String hostIp);

    void delHost(@Param("id") Integer id);
}
