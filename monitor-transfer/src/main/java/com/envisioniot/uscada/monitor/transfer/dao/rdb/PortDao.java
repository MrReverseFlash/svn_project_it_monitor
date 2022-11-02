package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.PortInfo;
import com.envisioniot.uscada.monitor.common.entity.PortStatSample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: PortDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:55
 */
public interface PortDao {

    void insertBatch(String hostIp, List<PortStatSample> list);

    void batchForUpdate(String hostIp, List<PortStatSample> list);

    List<PortInfo> getMonitorObj(String hostIp);
}
