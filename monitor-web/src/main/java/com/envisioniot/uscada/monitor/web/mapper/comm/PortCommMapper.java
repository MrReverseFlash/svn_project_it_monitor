package com.envisioniot.uscada.monitor.web.mapper.comm;

import com.envisioniot.uscada.monitor.web.entity.PortInfo;
import com.envisioniot.uscada.monitor.web.entity.PortSampleReq;

import java.util.List;

public interface PortCommMapper {

    List<PortInfo> qryAll();

    List<PortSampleReq> qryPortSampleReqByIds(List<Long> ids);
}
