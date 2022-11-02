package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.PortStatSample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PortMysqlMapper {

    void insertBatch(@Param("host_ip") String hostIp,
                     @Param("list") List<PortStatSample> list);

    void batchForUpdate(@Param("host_ip") String hostIp,
                        @Param("list") List<PortStatSample> list);
}
