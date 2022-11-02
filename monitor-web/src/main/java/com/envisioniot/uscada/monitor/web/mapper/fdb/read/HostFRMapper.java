package com.envisioniot.uscada.monitor.web.mapper.fdb.read;

import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HostFRMapper {

    List<AlarmResponse> getHostAlarm(@Param("st") String st,
                                     @Param("et") String et,
                                     @Param("hostIp") String hostIp);
}
