package com.envisioniot.uscada.monitor.web.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.web.dao.rdb.TopoCommDao;
import com.envisioniot.uscada.monitor.web.entity.TopoInfo;
import com.envisioniot.uscada.monitor.web.mapper.mysql.TopoMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: TopoMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 16:58
 */
public class TopoMysqlDao extends TopoCommDao {

    @Autowired
    private TopoMysqlMapper topoMysqlMapper;

    @Override
    public void insCustRelaByHost(String hostIpA, String hostIpB) {
        topoMysqlMapper.insCustRelaByHost(hostIpA, hostIpB);
    }

    @Override
    public void delCustRelaByHost(String hostIpA, String hostIpB) {
        topoMysqlMapper.delCustRelaByHost(hostIpA, hostIpB);
    }

    @Override
    public void insTopoParam(TopoParam param) {
        topoMysqlMapper.insTopoParam(param);
    }

    @Override
    public void updTopoParam(TopoParam param) {
        topoMysqlMapper.updTopoParam(param);
    }

    @Override
    public void updHostParamId(String id, short type, List<String> list) {
        topoMysqlMapper.updHostParamId(id, type, list);
    }

    @Override
    public void updHostCoordinate(List<TopoInfo> list) {
        topoMysqlMapper.updHostCoordinate(list);
    }

    @Override
    public void delHostParamId(short type, List<String> list) {
        topoMysqlMapper.delHostParamId(type, list);
    }

    @Override
    public void delTopoParam(String id) {
        topoMysqlMapper.delTopoParam(id);
    }
}
