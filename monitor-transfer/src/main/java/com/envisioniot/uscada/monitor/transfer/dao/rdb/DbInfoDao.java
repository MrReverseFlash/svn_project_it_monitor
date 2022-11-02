package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.DbStat;
import java.util.List;

/**
 * <p>Title: DbInfoDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/12 21:22
 */
public interface DbInfoDao {

    void batchForUpdate(String hostIp, List<DbStat> sampleData);

    void insertBatch(String hostIp, List<DbStat> dbInfo);

    void insert(String hostIp, DbStat db);

    String selectIdByInfo(String hostIp, DbStat db);

    List<DbStat> getMonitorDb(String hostIp);

    String qryDbAlarmObjById(String dbId);
}
