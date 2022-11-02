package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.ContainerStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DockerCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.DockerFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DockerFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 15:36
 */
public class DockerFdbDao extends DockerCommDao {

    @Autowired
    private DockerFWMapper dockerFWMapper;

    @Override
    public void insertBatch(String hostIp, List<ContainerStat> list) {
        dockerFWMapper.insertBatch(hostIp, list);
    }

    @Override
    public void batchForUpdate(String hostIp, List<ContainerStat> list) {
        dockerFWMapper.batchForUpdate_update_first(hostIp, list);
        dockerFWMapper.batchForUpdate_insert_later(hostIp, list);
    }
}
