package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.IedStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IedMysqlMapper {

    void insertBatch(@Param("host_ip") String hostIp,
                     @Param("list") List<IedStat> list);

    void batchForUpdate(@Param("host_ip") String hostIp,
                        @Param("list") List<IedStat> list);
}
