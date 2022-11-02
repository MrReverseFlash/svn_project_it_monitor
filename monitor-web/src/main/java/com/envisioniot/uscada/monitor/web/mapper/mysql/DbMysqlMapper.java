package com.envisioniot.uscada.monitor.web.mapper.mysql;

import com.envisioniot.uscada.monitor.web.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbMysqlMapper {

    int updateById(DbInfo dbInfo);

    void save(DbInfo dbInfo);

    int deleteByIds(List<String> ids);

    void saveTableInfo(SaveTableReq saveTableReq);

    int updTableById(SaveTableReq saveTableReq);

    int delTableByIds(List<Long> ids);

    int delTableByDbIds(List<String> dbIds);

    List<AlarmResponse> getDbAlarm(@Param("st") String st,
                                   @Param("et") String et,
                                   @Param("dbId") String dbId);
}
