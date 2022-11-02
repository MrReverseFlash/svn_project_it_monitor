package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.AlarmDbInfo;
import com.envisioniot.uscada.monitor.common.entity.AlarmHostInfo;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.AlarmCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.AlarmFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Title: AlarmFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 10:08
 */
public class AlarmFdbDao extends AlarmCommDao {

    @Autowired
    private AlarmFWMapper alarmFWMapper;

    @Override
    public void insertHostAlarm(AlarmHostInfo alarmHostInfo) {
        alarmFWMapper.insertHostAlarm(alarmHostInfo);
    }

    @Override
    public void insertDbAlarm(AlarmDbInfo alarmDbInfo) {
        alarmFWMapper.insertDbAlarm(alarmDbInfo);
    }
}
