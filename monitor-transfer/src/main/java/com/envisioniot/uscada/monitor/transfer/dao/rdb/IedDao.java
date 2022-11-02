package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.IedInfo;
import com.envisioniot.uscada.monitor.common.entity.IedStat;

import java.util.List;

/**
 * <p>Title: IedDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:39
 */
public interface IedDao {

    void insertBatch(String hostIp, List<IedStat> list);

    void batchForUpdate(String hostIp, List<IedStat> list);

    List<IedInfo> getMonitorObj(String hostIp);
}
