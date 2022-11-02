package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopoCommMapper {

    /**
     * 根据主机获取所有连线关系
     * @param hostIp
     * @return
     */
    List<String> qryRelaByHost(@Param("hostIp") String hostIp);

    /**
     * 查询出主机的集合ID
     * @param hostIp
     */
    String qrySetIdByHost(@Param("hostIp") String hostIp);

    /**
     * 查询出默认集合中已经存在的最小集合ID
     * @param list
     */
    String qryMinSetId(@Param("list") List<String> list);
}
