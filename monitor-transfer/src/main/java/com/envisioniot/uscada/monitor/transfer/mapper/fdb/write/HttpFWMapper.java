package com.envisioniot.uscada.monitor.transfer.mapper.fdb.write;

import com.envisioniot.uscada.monitor.common.entity.HttpStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HttpFWMapper {

    void insertBatch(@Param("host_ip") String hostIp,
                     @Param("list") List<HttpStat> list);

    void batchForUpdate_update_first(@Param("host_ip") String hostIp,
                                     @Param("list") List<HttpStat> list);

    void batchForUpdate_insert_later(@Param("host_ip") String hostIp,
                                     @Param("list") List<HttpStat> list);
}
