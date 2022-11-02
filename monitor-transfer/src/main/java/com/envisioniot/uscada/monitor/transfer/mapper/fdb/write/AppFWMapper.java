package com.envisioniot.uscada.monitor.transfer.mapper.fdb.write;

import com.envisioniot.uscada.monitor.common.entity.AppStat;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface AppFWMapper {

    /**
     * @param hostIp: agent ip
     * @param app:    app sample information
     */
    int update_update_first(@Param("hostIp") String hostIp,
                             @Param("item") AppStat app);

    /**
     * @param hostIp: agent ip
     * @param app:    app sample information
     */
    void update_insert_iffail(@Param("hostIp") String hostIp,
                              @Param("item") AppStat app);

    /**
     * @param hostIp agent ip
     * @param app:   app information
     */
    void insert(@Param("host_ip") String hostIp,
                @Param("app_list") List<AppStat> app);

    void batchForUpdate_update_first(@Param("host_ip") String hostIp,
                                     @Param("list") List<AppStat> sampleData);

    void batchForUpdate_insert_later(@Param("host_ip") String hostIp,
                                     @Param("list") List<AppStat> sampleData);

    void updateBatch(@Param("host_ip") String hostIp,
                      @Param("list") List<AppStat> sampleData);

}
