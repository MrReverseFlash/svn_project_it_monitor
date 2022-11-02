package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import java.util.List;

/**
 * <p>Title: DbTableDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 13:53
 */
public interface DbTableDao {

    void insertBatch(String id, List<TableInfo> tables);

    void batchForUpdate(String id, List<TableInfo> tables);

    List<TableInfo> getMonitorTable(String id);
}
