package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.AppTable;
import com.envisioniot.uscada.monitor.web.mapper.comm.AppCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: AppCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/14 15:28
 */
public abstract class AppCommDao implements AppDao{

    @Autowired
    protected AppCommMapper appCommMapper;

    @Override
    public List<AppTable> getAllApp() {
        return appCommMapper.getAllApp();
    }

    @Override
    public List<AppTable> getApp(String hostIp) {
        return appCommMapper.getApp(hostIp);
    }
}
