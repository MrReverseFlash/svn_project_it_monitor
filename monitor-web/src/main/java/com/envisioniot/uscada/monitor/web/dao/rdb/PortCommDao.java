package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.PortInfo;
import com.envisioniot.uscada.monitor.web.entity.PortSampleReq;
import com.envisioniot.uscada.monitor.web.mapper.comm.PortCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: PortCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 14:34
 */
public abstract class PortCommDao implements PortDao{

    @Autowired
    protected PortCommMapper portCommMapper;

    @Override
    public List<PortInfo> qryAll() {
        return portCommMapper.qryAll();
    }

    @Override
    public List<PortSampleReq> qryPortSampleReqByIds(List<Long> ids) {
        return portCommMapper.qryPortSampleReqByIds(ids);
    }
}
