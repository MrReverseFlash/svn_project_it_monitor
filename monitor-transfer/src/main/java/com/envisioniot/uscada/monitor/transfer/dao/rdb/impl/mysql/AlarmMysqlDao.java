package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.AlarmDbInfo;
import com.envisioniot.uscada.monitor.common.entity.AlarmHostInfo;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.AlarmCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.AlarmMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Title: AlarmMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/12 15:19
 */
public class AlarmMysqlDao extends AlarmCommDao {

    @Autowired
    private AlarmMysqlMapper alarmMysqlMapper;

    @Override
    public void insertHostAlarm(AlarmHostInfo alarmHostInfo) {
        alarmMysqlMapper.insertHostAlarm(alarmHostInfo);
    }

    @Override
    public void insertDbAlarm(AlarmDbInfo alarmDbInfo) {
        alarmMysqlMapper.insertDbAlarm(alarmDbInfo);
    }
}
