package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.DockerInfo;
import com.envisioniot.uscada.monitor.web.mapper.comm.DockerCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DockerCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 15:50
 */
public abstract class DockerCommDao implements DockerDao{

    @Autowired
    protected DockerCommMapper dockerCommMapper;

    @Override
    public List<DockerInfo> qryAll() {
        return dockerCommMapper.qryAll();
    }

    @Override
    public List<DockerInfo> query(String hostIp) {
        return dockerCommMapper.query(hostIp);
    }
}
