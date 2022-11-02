package com.envisioniot.uscada.monitor.transfer.mapper.fdb.write;

import com.envisioniot.uscada.monitor.common.entity.AlarmDbInfo;
import com.envisioniot.uscada.monitor.common.entity.AlarmHostInfo;
import org.apache.ibatis.annotations.Param;

public interface AlarmFWMapper {

    void insertHostAlarm(@Param("alarmHostInfo") AlarmHostInfo alarmHostInfo);

    void insertDbAlarm(@Param("alarmDbInfo") AlarmDbInfo alarmDbInfo);
}
