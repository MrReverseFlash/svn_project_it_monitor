package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.AppStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppMysqlMapper {

    /**
     * @param hostIp: agent ip
     * @param app:    app sample information
     */
    void update(@Param("hostIp") String hostIp,
                @Param("item") AppStat app);

    /**
     * @param hostIp agent ip
     * @param app:   app information
     */
    void insert(@Param("host_ip") String hostIp,
                @Param("app_list") List<AppStat> app);

    void batchForUpdate(@Param("host_ip") String hostIp,
                     @Param("list") List<AppStat> sampleData);

    void updateBatch(@Param("host_ip") String hostIp,
                      @Param("list") List<AppStat> sampleData);

}
