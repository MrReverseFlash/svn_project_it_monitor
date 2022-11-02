package com.envisioniot.uscada.monitor.web.mapper.mysql;

import com.envisioniot.uscada.monitor.web.entity.PortInfo;

import java.util.List;

public interface PortMysqlMapper {

    int updateById(PortInfo portInfo);

    void save(PortInfo portInfo);

    int deleteByIds(List<Long> ids);
}
