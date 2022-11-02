package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.transfer.mapper.comm.DockerCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DockerCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 15:33
 */
public abstract class DockerCommDao implements DockerDao{

    @Autowired
    protected DockerCommMapper dockerCommMapper;

    @Override
    public List<String> getMonitorContainerIds(String hostIp) {
        return dockerCommMapper.getMonitorContainerIds(hostIp);
    }
}
