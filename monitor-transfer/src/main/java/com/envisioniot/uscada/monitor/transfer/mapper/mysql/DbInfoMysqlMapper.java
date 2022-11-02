package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.DbStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbInfoMysqlMapper {

    void batchForUpdate(@Param("host_ip") String hostIp,
                        @Param("list") List<DbStat> sampleData);

    void insertBatch(@Param("host_ip") String hostIp,
                     @Param("list") List<DbStat> dbInfo);

    void insert(@Param("host_ip") String hostIp,
                @Param("db") DbStat db);

    String qryDbAlarmObjById(@Param("dbId") String dbId);

    String selectIdByInfo(@Param("host_ip") String hostIp,
                          @Param("db") DbStat db);
}
