package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.AppObj;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.AppDao;
import com.envisioniot.uscada.monitor.transfer.mapper.comm.AppCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: AppCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/10 15:36
 */
public abstract class AppCommDao implements AppDao {

    @Autowired
    protected AppCommMapper appCommMapper;

    @Override
    public List<AppObj> getMonitorApps(String hostIp) {
        return appCommMapper.getMonitorApps(hostIp);
    }
}
