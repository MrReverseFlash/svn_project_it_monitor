package com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.web.dao.rdb.AppCommDao;
import com.envisioniot.uscada.monitor.web.mapper.fdb.write.AppFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Title: AppFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/14 15:33
 */
public class AppFdbDao extends AppCommDao {

    @Autowired
    private AppFWMapper appFWMapper;

    @Override
    public void modifyName(Integer id, String name) {
        appFWMapper.modifyName(id, name);
    }

    @Override
    public void deleteApp(Integer id) {
        appFWMapper.deleteApp(id);
    }

    @Override
    public void addMonitorApp(String appPid, String appUid, String hostIp, String name, String containerName) {
        appFWMapper.addMonitorApp(appPid, appUid, hostIp, name, containerName);
    }
}
