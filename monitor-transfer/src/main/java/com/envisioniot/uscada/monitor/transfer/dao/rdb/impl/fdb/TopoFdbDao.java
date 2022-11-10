package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.GatewayInfo;
import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.common.entity.TopoRelaInfo;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.TopoCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.read.TopoFRMapper;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.TopoFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * <p>Title: TopoFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/14 14:33
 */
public class TopoFdbDao extends TopoCommDao {

    @Autowired
    private TopoFWMapper topoFWMapper;

    @Autowired
    private TopoFRMapper topoFRMapper;

    @Override
    public void insHost(String hostIp) {
        int updNum = topoFWMapper.insHost_update_first(hostIp);
        if (updNum <= 0) {
            topoFWMapper.insHost_insert_iffail(hostIp);
        }
    }

    @Override
    public void insAssociatedHost(List<TopoRelaInfo> list) {
        topoFWMapper.insAssociatedHost(list);
    }

    @Override
    public void delNotExtDefRelaByHost(String hostIp, List<TopoRelaInfo> list) {
        topoFWMapper.delNotExtDefRelaByHost(hostIp, list);
    }

    @Override
    public void delNotExtDefRelaByHost(String hostIp) {
        topoFWMapper.delDefRelaByHost(hostIp);
    }

    @Override
    public void insDefRelaByHost(String hostIp, List<TopoRelaInfo> list) {
        topoFWMapper.insDefRelaByHost_update_first(hostIp, list);
        topoFWMapper.insDefRelaByHost_insert_later(hostIp, list);
    }

    @Override
    public void updRelaList(String hostIp, List<TopoRelaInfo> list) {
        topoFWMapper.updRelaList(hostIp, list);
    }

    @Override
    public void delNotExtGwByHost(String hostIp, List<GatewayInfo> list) {
        topoFWMapper.delNotExtGwByHost(hostIp, list);
    }

    @Override
    public void insGwList(String hostIp, List<GatewayInfo> list) {
        topoFWMapper.insGwList(hostIp, list);
    }

    @Override
    public void updGwList(String hostIp, List<GatewayInfo> list) {
        topoFWMapper.updGwList(hostIp, list);
    }

    @Override
    public void updSetId(String topoSetId, List<String> list) {
        topoFWMapper.updSetId(topoSetId, list);
    }

    @Override
    public void insTopoParam(TopoParam param) {
        topoFWMapper.insTopoParam_insert_later(param);
    }
}
