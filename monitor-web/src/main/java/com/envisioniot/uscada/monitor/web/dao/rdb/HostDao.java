package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.entity.HostPropResp;
import com.envisioniot.uscada.monitor.web.entity.HostTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: HostDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 10:27
 */
public interface HostDao {

    /**
     * 获取主机监控的时序数据
     */
    List<HostTable> getTimeSeriesList();

    void modifyLabel(Integer id, String label);

    HostTable getInfo(String hostIp);

    List<HostPropResp> getHostProps(String matchFlag);

    List<AlarmResponse> getHostAlarm(String st, String et, String hostIp);

    void delHost(Integer id);
}
