package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.ContainerStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DockerCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.DockerMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DockerMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 15:35
 */
public class DockerMysqlDao extends DockerCommDao {

    @Autowired
    private DockerMysqlMapper dockerMysqlMapper;

    @Override
    public void insertBatch(String hostIp, List<ContainerStat> list) {
        dockerMysqlMapper.insertBatch(hostIp, list);
    }

    @Override
    public void batchForUpdate(String hostIp, List<ContainerStat> list) {
        dockerMysqlMapper.batchForUpdate(hostIp, list);
    }
}
