package com.envisioniot.uscada.monitor.web.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.web.dao.rdb.HostCommDao;
import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.mapper.mysql.HostMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HostMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 10:35
 */
public class HostMysqlDao extends HostCommDao {

    @Autowired
    private HostMysqlMapper hostMysqlMapper;

    @Override
    public void modifyLabel(Integer id, String label) {
        hostMysqlMapper.modifyLabel(id, label);
    }

    @Override
    public List<AlarmResponse> getHostAlarm(String st, String et, String hostIp) {
        return hostMysqlMapper.getHostAlarm(st, et, hostIp);
    }

    @Override
    public void delHost(Integer id) {
        hostMysqlMapper.delHost(id);
    }
}
