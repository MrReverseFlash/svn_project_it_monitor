package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.HttpInfo;
import com.envisioniot.uscada.monitor.transfer.mapper.comm.HttpCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HttpCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:22
 */
public abstract class HttpCommDao implements HttpDao{

    @Autowired
    protected HttpCommMapper httpCommMapper;

    @Override
    public List<HttpInfo> getMonitorObj(String hostIp) {
        return httpCommMapper.getMonitorObj(hostIp);
    }
}
