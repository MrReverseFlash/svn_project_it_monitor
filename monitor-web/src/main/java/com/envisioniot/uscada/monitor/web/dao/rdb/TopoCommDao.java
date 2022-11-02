package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.web.entity.GatewayInfo;
import com.envisioniot.uscada.monitor.web.entity.TopoInfo;
import com.envisioniot.uscada.monitor.web.entity.TopoRela;
import com.envisioniot.uscada.monitor.web.mapper.comm.TopoCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: TopoCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 16:49
 */
public abstract class TopoCommDao implements TopoDao{

    @Autowired
    protected TopoCommMapper topoCommMapper;

    @Override
    public List<TopoInfo> qryTopoInfo() {
        return topoCommMapper.qryTopoInfo();
    }

    @Override
    public List<GatewayInfo> qryGatewayByHost(String hostIp) {
        return topoCommMapper.qryGatewayByHost(hostIp);
    }

    @Override
    public List<TopoRela> qryTopoRelaByHost(String hostIp) {
        return topoCommMapper.qryTopoRelaByHost(hostIp);
    }

    @Override
    public List<TopoParam> qryTopoParamByType(short type) {
        return topoCommMapper.qryTopoParamByType(type);
    }

    @Override
    public String qryHostByParamId(String id, short type) {
        return topoCommMapper.qryHostByParamId(id, type);
    }
}
