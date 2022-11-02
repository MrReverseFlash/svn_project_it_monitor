package com.envisioniot.uscada.monitor.web.mapper.comm;

import com.envisioniot.uscada.monitor.web.entity.AppTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
public interface AppCommMapper {

    List<AppTable> getAllApp();

    List<AppTable> getApp(@Param("host_ip") String hostIp);
}
