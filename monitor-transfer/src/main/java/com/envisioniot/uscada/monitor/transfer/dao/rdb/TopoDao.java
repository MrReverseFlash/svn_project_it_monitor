package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.GatewayInfo;
import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.common.entity.TopoRelaInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: TopoDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/14 14:20
 */
public interface TopoDao {

    /**
     * 插入拓扑单元--部署了Agent的主机
     * @param hostIp
     */
    void insHost(String hostIp);

    /**
     * 插入拓扑单元--上面部署了Agent的主机的关联主机（可能是数据库、前置机、client等等）
     * @param list
     */
    void insAssociatedHost(List<TopoRelaInfo> list);

    /**
     * 删除不存在的默认连线关系
     * @param hostIp
     * @param list
     */
    void delNotExtDefRelaByHost(String hostIp, List<TopoRelaInfo> list);

    /**
     * 插入默认的连线关系
     * @param hostIp
     * @param list
     */
    void insDefRelaByHost(String hostIp, List<TopoRelaInfo> list);

    /**
     * 根据主机获取所有连线关系
     * @param hostIp
     * @return
     */
    List<String> qryRelaByHost(String hostIp);

    /**
     * 更新连线状态
     * @param hostIp
     * @param list
     */
    void updRelaList(String hostIp, List<TopoRelaInfo> list);

    /**
     * 根据主机删除不存在的网关
     * @param hostIp
     * @param list
     */
    void delNotExtGwByHost(String hostIp, List<GatewayInfo> list);

    /**
     * 插入主机的网关列表
     * @param hostIp
     * @param list
     */
    void insGwList(String hostIp, List<GatewayInfo> list);

    /**
     * 更新网关状态
     * @param hostIp
     * @param list
     */
    void updGwList(String hostIp, List<GatewayInfo> list);


    /**
     * 查询出主机的集合ID
     * @param hostIp
     */
    String qrySetIdByHost(String hostIp);

    /**
     * 查询出默认集合中已经存在的最小集合ID
     * @param list
     */
    String qryMinSetId(List<String> list);

    /**
     * 更新集合的集合ID
     * @param topoSetId
     * @param list
     */
    void updSetId(String topoSetId, List<String> list);

    /**
     * 新增拓扑参数并返回自增ID
     * @param param
     */
    void insTopoParam(TopoParam param);
}
