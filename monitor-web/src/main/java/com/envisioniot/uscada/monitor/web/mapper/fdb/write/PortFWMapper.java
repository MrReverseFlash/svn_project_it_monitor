package com.envisioniot.uscada.monitor.web.mapper.fdb.write;

import com.envisioniot.uscada.monitor.web.entity.PortInfo;

import java.util.List;

public interface PortFWMapper {

    int updateById(PortInfo portInfo);

    void save(PortInfo portInfo);

    int deleteByIds(List<Long> ids);
}
