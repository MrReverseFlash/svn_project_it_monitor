package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.ContainerStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: DockerDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 15:32
 */
public interface DockerDao {
    void insertBatch(String hostIp, List<ContainerStat> list);

    void batchForUpdate(String hostIp, List<ContainerStat> list);

    List<String> getMonitorContainerIds(String hostIp);
}
