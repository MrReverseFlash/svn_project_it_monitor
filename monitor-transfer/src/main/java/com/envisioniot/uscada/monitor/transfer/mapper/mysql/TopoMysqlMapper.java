package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.GatewayInfo;
import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.common.entity.TopoRelaInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopoMysqlMapper {

    /**
     * 插入拓扑单元--部署了Agent的主机
     * @param hostIp
     */
    void insHost(@Param("hostIp") String hostIp);

    /**
     * 插入拓扑单元--上面部署了Agent的主机的关联主机（可能是数据库、前置机、client等等）
     * @param list
     */
    void insAssociatedHost(@Param("list") List<TopoRelaInfo> list);

    /**
     * 删除不存在的默认连线关系
     * @param hostIp
     * @param list
     */
    void delNotExtDefRelaByHost(@Param("hostIp") String hostIp,
                                @Param("list") List<TopoRelaInfo> list);

    /**
     * 插入默认的连线关系
     * @param hostIp
     * @param list
     */
    void insDefRelaByHost(@Param("hostIp") String hostIp,
                          @Param("list") List<TopoRelaInfo> list);

    /**
     * 更新连线状态
     * @param hostIp
     * @param list
     */
    void updRelaList(@Param("hostIp") String hostIp,
                     @Param("list") List<TopoRelaInfo> list);

    /**
     * 根据主机删除不存在的网关
     * @param hostIp
     * @param list
     */
    void delNotExtGwByHost(@Param("hostIp") String hostIp,
                           @Param("list")List<GatewayInfo> list);

    /**
     * 插入主机的网关列表
     * @param hostIp
     * @param list
     */
    void insGwList(@Param("hostIp") String hostIp,
                   @Param("list")List<GatewayInfo> list);

    /**
     * 更新网关状态
     * @param hostIp
     * @param list
     */
    void updGwList(@Param("hostIp") String hostIp,
                   @Param("list")List<GatewayInfo> list);

    /**
     * 更新集合的集合ID
     * @param topoSetId
     * @param list
     */
    void updSetId(@Param("topoSetId") String topoSetId,
                  @Param("list") List<String> list);

    /**
     * 新增拓扑参数并返回自增ID
     * @param param
     */
    void insTopoParam(@Param("param") TopoParam param);

    void delDefRelaByHost(@Param("hostIp") String hostIp);

}
