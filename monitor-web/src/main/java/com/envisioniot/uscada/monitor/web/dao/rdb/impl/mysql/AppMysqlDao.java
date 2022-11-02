package com.envisioniot.uscada.monitor.web.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.web.dao.rdb.AppCommDao;
import com.envisioniot.uscada.monitor.web.mapper.mysql.AppMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Title: AppMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/14 15:32
 */
public class AppMysqlDao extends AppCommDao {

    @Autowired
    private AppMysqlMapper appMysqlMapper;

    @Override
    public void modifyName(Integer id, String name) {
        appMysqlMapper.modifyName(id, name);
    }

    @Override
    public void deleteApp(Integer id) {
        appMysqlMapper.deleteApp(id);
    }

    @Override
    public void addMonitorApp(String appPid, String appUid, String hostIp, String name, String containerName) {
        appMysqlMapper.addMonitorApp(appPid, appUid, hostIp, name, containerName);
    }
}
