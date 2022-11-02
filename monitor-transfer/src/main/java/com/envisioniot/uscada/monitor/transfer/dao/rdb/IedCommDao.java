package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.IedInfo;
import com.envisioniot.uscada.monitor.transfer.mapper.comm.IedCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: IedCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:40
 */
public abstract class IedCommDao implements IedDao{

    @Autowired
    protected IedCommMapper iedCommMapper;

    @Override
    public List<IedInfo> getMonitorObj(String hostIp) {
        return iedCommMapper.getMonitorObj(hostIp);
    }
}
