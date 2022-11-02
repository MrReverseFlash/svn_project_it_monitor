package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.transfer.mapper.comm.TopoCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: TopoCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/14 14:27
 */
public abstract class TopoCommDao implements TopoDao{

    @Autowired
    protected TopoCommMapper topoCommMapper;

    @Override
    public List<String> qryRelaByHost(String hostIp) {
        return topoCommMapper.qryRelaByHost(hostIp);
    }

    @Override
    public String qrySetIdByHost(String hostIp) {
        return topoCommMapper.qrySetIdByHost(hostIp);
    }

    @Override
    public String qryMinSetId(List<String> list) {
        return topoCommMapper.qryMinSetId(list);
    }
}
