package com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.web.dao.rdb.HostCommDao;
import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.mapper.fdb.read.HostFRMapper;
import com.envisioniot.uscada.monitor.web.mapper.fdb.write.HostFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HostFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 10:41
 */
public class HostFdbDao extends HostCommDao {

    @Autowired
    private HostFRMapper hostFRMapper;

    @Autowired
    private HostFWMapper hostFWMapper;

    @Override
    public void modifyLabel(Integer id, String label) {
        hostFWMapper.modifyLabel(id, label);
    }

    @Override
    public List<AlarmResponse> getHostAlarm(String st, String et, String hostIp) {
        return hostFRMapper.getHostAlarm(st, et, hostIp);
    }

    @Override
    public void delHost(Integer id) {
        hostFWMapper.delHost(id);
    }
}
