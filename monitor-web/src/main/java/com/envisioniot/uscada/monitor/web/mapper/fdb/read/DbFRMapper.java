package com.envisioniot.uscada.monitor.web.mapper.fdb.read;

import com.envisioniot.uscada.monitor.web.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbFRMapper {

    List<AlarmResponse> getDbAlarm(@Param("st") String st,
                                   @Param("et") String et,
                                   @Param("dbId") String dbId);
}
