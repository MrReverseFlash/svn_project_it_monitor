package com.envisioniot.uscada.monitor.web.mapper.fdb.write;

import org.apache.ibatis.annotations.Param;

public interface HostFWMapper {

    void modifyLabel(@Param("id") Integer id,
                     @Param("label") String label);

    void delHost(@Param("id")Integer id);
}
