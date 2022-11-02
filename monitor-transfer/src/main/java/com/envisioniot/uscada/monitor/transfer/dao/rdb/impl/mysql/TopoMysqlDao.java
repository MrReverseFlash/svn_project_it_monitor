package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.GatewayInfo;
import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.common.entity.TopoRelaInfo;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.TopoCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.TopoMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: TopoMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/14 14:30
 */
public class TopoMysqlDao extends TopoCommDao {

    @Autowired
    private TopoMysqlMapper topoMysqlMapper;

    @Override
    public void insHost(String hostIp) {
        topoMysqlMapper.insHost(hostIp);
    }

    @Override
    public void insAssociatedHost(List<TopoRelaInfo> list) {
        topoMysqlMapper.insAssociatedHost(list);
    }

    @Override
    public void delNotExtDefRelaByHost(String hostIp, List<TopoRelaInfo> list) {
        topoMysqlMapper.delNotExtDefRelaByHost(hostIp, list);
    }

    @Override
    public void insDefRelaByHost(String hostIp, List<TopoRelaInfo> list) {
        topoMysqlMapper.insDefRelaByHost(hostIp, list);
    }

    @Override
    public void updRelaList(String hostIp, List<TopoRelaInfo> list) {
        topoMysqlMapper.updRelaList(hostIp, list);
    }

    @Override
    public void delNotExtGwByHost(String hostIp, List<GatewayInfo> list) {
        topoMysqlMapper.delNotExtGwByHost(hostIp, list);
    }

    @Override
    public void insGwList(String hostIp, List<GatewayInfo> list) {
        topoMysqlMapper.insGwList(hostIp, list);
    }

    @Override
    public void updGwList(String hostIp, List<GatewayInfo> list) {
        topoMysqlMapper.updGwList(hostIp, list);
    }

    @Override
    public void updSetId(String topoSetId, List<String> list) {
        topoMysqlMapper.updSetId(topoSetId, list);
    }

    @Override
    public void insTopoParam(TopoParam param) {
        topoMysqlMapper.insTopoParam(param);
    }
}
