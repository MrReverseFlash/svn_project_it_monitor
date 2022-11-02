package com.envisioniot.uscada.monitor.web.mapper.comm;

import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.web.entity.GatewayInfo;
import com.envisioniot.uscada.monitor.web.entity.TopoInfo;
import com.envisioniot.uscada.monitor.web.entity.TopoRela;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TopoCommMapper
 *
 * @author yangkang
 * @date 2021/3/2
 */
public interface TopoCommMapper {

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
    List<GatewayInfo> qryGatewayByHost(@Param("hostIp") String hostIp);

    /**
     * 根据主机IP查询相关的连线状态信息
     * @param hostIp
     * @return
     */
    List<TopoRela> qryTopoRelaByHost(@Param("hostIp") String hostIp);

    /**
     * 根据类型查询对应的所有拓扑参数
     * @param type
     * @return
     */
    List<TopoParam> qryTopoParamByType(@Param("type") short type);

    /**
     * 查询层级ID、集合ID、物理ID关联的一台主机
     * @param id
     * @param type
     */
    String qryHostByParamId(@Param("id") String id,
                            @Param("type") short type);
}
