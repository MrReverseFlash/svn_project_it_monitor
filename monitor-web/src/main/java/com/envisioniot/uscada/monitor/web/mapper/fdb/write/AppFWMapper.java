package com.envisioniot.uscada.monitor.web.mapper.fdb.write;

import org.apache.ibatis.annotations.Param;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
public interface AppFWMapper {

    /**
     * @param id app id
     * @param name app name
     */
    void modifyName(@Param("id") Integer id,
                    @Param("app_name") String name);

    void deleteApp(@Param("id") Integer id);

    void addMonitorApp(@Param("app_pid")String appPid,
                       @Param("app_uid") String appUid,
                       @Param("host_ip") String hostIp,
                       @Param("app_name") String name,
                       @Param("container_name") String containerName);

}
