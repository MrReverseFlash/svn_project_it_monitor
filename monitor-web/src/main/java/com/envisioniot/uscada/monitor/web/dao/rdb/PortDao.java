package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.PortInfo;
import com.envisioniot.uscada.monitor.web.entity.PortSampleReq;

import java.util.List;

/**
 * <p>Title: PortDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 14:33
 */
public interface PortDao {

    List<PortInfo> qryAll();

    int updateById(PortInfo portInfo);

    void save(PortInfo portInfo);

    int deleteByIds(List<Long> ids);

    List<PortSampleReq> qryPortSampleReqByIds(List<Long> ids);
}
