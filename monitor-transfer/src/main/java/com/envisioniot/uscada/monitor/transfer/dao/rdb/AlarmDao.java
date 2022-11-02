package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.AlarmDbInfo;
import com.envisioniot.uscada.monitor.common.entity.AlarmHostInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>Title: AlarmDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/12 15:13
 */
public interface AlarmDao {

    void insertHostAlarm(AlarmHostInfo alarmHostInfo);

    void insertDbAlarm(AlarmDbInfo alarmDbInfo);
}
