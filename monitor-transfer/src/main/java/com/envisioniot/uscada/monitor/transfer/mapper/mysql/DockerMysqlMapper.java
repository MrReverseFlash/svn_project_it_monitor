package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.ContainerStat;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DockerMysqlMapper {

    void insertBatch(@Param("host_ip") String hostIp,
                     @Param("list") List<ContainerStat> list);

    void batchForUpdate(@Param("host_ip") String hostIp,
                        @Param("list") List<ContainerStat> list);
}