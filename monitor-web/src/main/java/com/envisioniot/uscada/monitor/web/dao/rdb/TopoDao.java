package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.web.entity.GatewayInfo;
import com.envisioniot.uscada.monitor.web.entity.TopoInfo;
import com.envisioniot.uscada.monitor.web.entity.TopoRela;

import java.util.List;

/**
 * <p>Title: TopoDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 16:47
 */
public interface TopoDao {

    /**
     * 查询所有拓扑单元及相关信息
     * @return
     */
    List<TopoInfo> qryTopoInfo();

    /**
     * 根据主机IP查询相关的网关状态信息
     * @param hostIp
     * @return
     */
    List<GatewayInfo> qryGatewayByHost(String hostIp);

    /**
     * 根据主机IP查询相关的连线状态信息
     * @param hostIp
     * @return
     */
    List<TopoRela> qryTopoRelaByHost(String hostIp);

    /**
     * 插入自定义的连线关系（插入两条互易记录）
     * @param hostIpA
     * @param hostIpB
     */
    void insCustRelaByHost(String hostIpA, String hostIpB);

    /**
     * 删除自定义的连线关系（删除两条互易记录）
     * @param hostIpA
     * @param hostIpB
     */
    void delCustRelaByHost(String hostIpA, String hostIpB);

    /**
     * 根据类型查询对应的所有拓扑参数
     * @param type
     * @return
     */
    List<TopoParam> qryTopoParamByType(short type);

    /**
     * 新增拓扑参数并返回自增ID
     * @param param
     */
    void insTopoParam(TopoParam param);

    /**
     * 更新拓扑参数的序号或者名称
     * @param param
     */
    void updTopoParam(TopoParam param);

    /**
     * 更新集合的层级ID、集合ID、物理ID
     * @param id
     * @param type
     * @param list
     */
    void updHostParamId(String  id, short type, List<String> list);

    /**
     * 更新拓扑单元的坐标
     * @param list
     */
    void updHostCoordinate(List<TopoInfo> list);

    /**
     * 去掉集合关联的层级ID、集合ID、物理ID
     * @param type
     * @param list
     */
    void delHostParamId(short type, List<String> list);

    /**
     * 查询层级ID、集合ID、物理ID关联的一台主机
     * @param id
     * @param type
     */
    String qryHostByParamId(String id, short type);

    /**
     * 删除层级ID、集合ID、物理ID对应的拓扑参数
     * @param id
     */
    void delTopoParam(String id);
}
