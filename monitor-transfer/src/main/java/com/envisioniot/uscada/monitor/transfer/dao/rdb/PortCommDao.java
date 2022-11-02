package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.PortInfo;
import com.envisioniot.uscada.monitor.transfer.mapper.comm.PortCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: PortCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:56
 */
public abstract class PortCommDao implements PortDao{

    @Autowired
    protected PortCommMapper portCommMapper;

    @Override
    public List<PortInfo> getMonitorObj(String hostIp) {
        return portCommMapper.getMonitorObj(hostIp);
    }
}
