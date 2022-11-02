package com.envisioniot.uscada.monitor.transfer.mapper.fdb.read;

import com.envisioniot.uscada.monitor.common.entity.DbStat;
import org.apache.ibatis.annotations.Param;

public interface DbInfoFRMapper {

    Long insert_getid_ifsuccess(@Param("host_ip") String hostIp,
                                @Param("db") DbStat db);

    String qryDbAlarmObjById(@Param("dbId") String dbId);
}
