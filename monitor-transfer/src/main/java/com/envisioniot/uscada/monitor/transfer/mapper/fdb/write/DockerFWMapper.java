package com.envisioniot.uscada.monitor.transfer.mapper.fdb.write;

import com.envisioniot.uscada.monitor.common.entity.ContainerStat;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DockerFWMapper {
    void insertBatch(@Param("host_ip") String hostIp,
                     @Param("list") List<ContainerStat> list);

    void batchForUpdate_update_first(@Param("host_ip") String hostIp,
                                     @Param("list") List<ContainerStat> list);

    void batchForUpdate_insert_later(@Param("host_ip") String hostIp,
                                     @Param("list") List<ContainerStat> list);
}