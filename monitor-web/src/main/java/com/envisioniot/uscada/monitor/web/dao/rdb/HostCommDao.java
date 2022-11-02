package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.HostPropResp;
import com.envisioniot.uscada.monitor.web.entity.HostTable;
import com.envisioniot.uscada.monitor.web.mapper.comm.HostCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HostCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 10:34
 */
public abstract class HostCommDao implements HostDao{

    @Autowired
    protected HostCommMapper hostCommMapper;

    @Override
    public List<HostTable> getTimeSeriesList() {
        return hostCommMapper.getTimeSeriesList();
    }

    @Override
    public HostTable getInfo(String hostIp) {
        return hostCommMapper.getInfo(hostIp);
    }

    @Override
    public List<HostPropResp> getHostProps(String matchFlag) {
        return hostCommMapper.getHostProps(matchFlag);
    }
}
