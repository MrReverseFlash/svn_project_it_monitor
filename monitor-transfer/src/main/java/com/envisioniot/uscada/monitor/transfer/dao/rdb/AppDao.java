package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.AppObj;
import com.envisioniot.uscada.monitor.common.entity.AppStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Title: AppInfluxDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/10 15:35
 */
public interface AppDao {

    void update(String hostIp, AppStat app);

    void insert(String hostIp, List<AppStat> app);

    void batchForUpdate(String hostIp, List<AppStat> sampleData);

    void updateBatch(String hostIp, List<AppStat> sampleData);

    List<AppObj> getMonitorApps(String hostIp);
}
