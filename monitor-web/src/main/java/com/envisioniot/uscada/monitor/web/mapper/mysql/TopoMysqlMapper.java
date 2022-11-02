package com.envisioniot.uscada.monitor.web.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.web.entity.TopoInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TopoMysqlMapper
 *
 * @author yangkang
 * @date 2021/3/2
 */
public interface TopoMysqlMapper {

    /**
     * 插入自定义的连线关系（插入两条互易记录）
     * @param hostIpA
     * @param hostIpB
     */
    void insCustRelaByHost(@Param("hostIpA") String hostIpA,
                           @Param("hostIpB") String hostIpB);

    /**
     * 删除自定义的连线关系（删除两条互易记录）
     * @param hostIpA
     * @param hostIpB
     */
    void delCustRelaByHost(@Param("hostIpA") String hostIpA,
                           @Param("hostIpB") String hostIpB);

    /**
     * 新增拓扑参数并返回自增ID
     * @param param
     */
    void insTopoParam(@Param("param") TopoParam param);

    /**
     * 更新拓扑参数的序号或者名称
     * @param param
     */
    void updTopoParam(@Param("param") TopoParam param);

    /**
     * 更新集合的层级ID、集合ID、物理ID
     * @param id
     * @param type
     * @param list
     */
    void updHostParamId(@Param("id") String id,
                        @Param("type") short type,
                        @Param("list") List<String> list);

    /**
     * 更新拓扑单元的坐标
     * @param list
     */
    void updHostCoordinate(@Param("list") List<TopoInfo> list);

    /**
     * 去掉集合关联的层级ID、集合ID、物理ID
     * @param type
     * @param list
     */
    void delHostParamId(@Param("type") short type,
                        @Param("list") List<String> list);

    /**
     * 删除层级ID、集合ID、物理ID对应的拓扑参数
     * @param id
     */
    void delTopoParam(@Param("id") String id);
}
