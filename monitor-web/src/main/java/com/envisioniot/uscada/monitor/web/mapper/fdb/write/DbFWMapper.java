package com.envisioniot.uscada.monitor.web.mapper.fdb.write;

import com.envisioniot.uscada.monitor.web.entity.*;

import java.util.List;

public interface DbFWMapper {

    int updateById(DbInfo dbInfo);

    void save(DbInfo dbInfo);

    int deleteByIds(List<String> ids);

    void saveTableInfo(SaveTableReq saveTableReq);

    int updTableById(SaveTableReq saveTableReq);

    int delTableByIds(List<Long> ids);

    int delTableByDbIds(List<String> dbIds);
}
