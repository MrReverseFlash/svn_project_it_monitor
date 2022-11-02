package com.envisioniot.uscada.monitor.transfer.mapper.fdb.write;

import com.envisioniot.uscada.monitor.common.entity.DbStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbInfoFWMapper {

    void batchForUpdate_update_first(@Param("host_ip") String hostIp,
                                     @Param("list") List<DbStat> sampleData);

    void batchForUpdate_insert_later(@Param("host_ip") String hostIp,
                                     @Param("list") List<DbStat> sampleData);

    void insertBatch(@Param("host_ip") String hostIp,
                     @Param("list") List<DbStat> dbInfo);

    int insert_insert_first(@Param("host_ip") String hostIp,
                            @Param("db") DbStat db);

    String selectIdByInfo(@Param("host_ip") String hostIp,
                          @Param("db") DbStat db);
}
