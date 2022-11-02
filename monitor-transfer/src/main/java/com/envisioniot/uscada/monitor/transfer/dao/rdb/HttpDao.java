package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.HttpInfo;
import com.envisioniot.uscada.monitor.common.entity.HttpStat;

import java.util.List;

/**
 * <p>Title: HttpDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:20
 */
public interface HttpDao {

    void insertBatch(String hostIp, List<HttpStat> list);

    void batchForUpdate(String hostIp, List<HttpStat> list);

    List<HttpInfo> getMonitorObj(String hostIp);
}
