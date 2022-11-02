package com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.web.dao.rdb.TopoCommDao;
import com.envisioniot.uscada.monitor.web.entity.TopoInfo;
import com.envisioniot.uscada.monitor.web.mapper.fdb.read.TopoFRMapper;
import com.envisioniot.uscada.monitor.web.mapper.fdb.write.TopoFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: TopoFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 17:01
 */
public class TopoFdbDao extends TopoCommDao {

    @Autowired
    private TopoFRMapper topoFRMapper;

    @Autowired
    private TopoFWMapper topoFWMapper;

    @Override
    public void insCustRelaByHost(String hostIpA, String hostIpB) {
        topoFWMapper.insCustRelaByHost(hostIpA, hostIpB);
    }

    @Override
    public void delCustRelaByHost(String hostIpA, String hostIpB) {
        topoFWMapper.delCustRelaByHost(hostIpA, hostIpB);
    }

    @Override
    public void insTopoParam(TopoParam param) {
        topoFWMapper.insTopoParam_insert_later(param);
    }

    @Override
    public void updTopoParam(TopoParam param) {
        topoFWMapper.updTopoParam(param);
    }

    @Override
    public void updHostParamId(String id, short type, List<String> list) {
        topoFWMapper.updHostParamId(id, type, list);
    }

    @Override
    public void updHostCoordinate(List<TopoInfo> list) {
        topoFWMapper.updHostCoordinate(list);
    }

    @Override
    public void delHostParamId(short type, List<String> list) {
        topoFWMapper.delHostParamId(type, list);
    }

    @Override
    public void delTopoParam(String id) {
        topoFWMapper.delTopoParam(id);
    }
}
