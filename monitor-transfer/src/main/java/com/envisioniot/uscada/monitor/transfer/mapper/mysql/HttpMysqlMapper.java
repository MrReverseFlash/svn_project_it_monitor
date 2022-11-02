package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.HttpStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HttpMysqlMapper {

    void insertBatch(@Param("host_ip") String hostIp,
                     @Param("list") List<HttpStat> list);

    void batchForUpdate(@Param("host_ip") String hostIp,
                        @Param("list") List<HttpStat> list);
}
