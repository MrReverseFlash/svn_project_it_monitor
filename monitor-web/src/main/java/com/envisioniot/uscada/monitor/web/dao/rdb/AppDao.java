package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.AppTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: AppDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/14 15:26
 */
public interface AppDao {

    List<AppTable> getAllApp();

    List<AppTable> getApp(String hostIp);

    /**
     * @param id app id
     * @param name app name
     */
    void modifyName(Integer id, String name);

    void deleteApp(Integer id);

    void addMonitorApp(String appPid, String appUid, String hostIp, String name, String containerName);

}
