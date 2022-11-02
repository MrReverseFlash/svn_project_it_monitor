package com.envisioniot.uscada.monitor.transfer.mapper.mysql;

import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DbTableMysqlMapper {

    void insertBatch(@Param("db_id") String id,
                     @Param("list") List<TableInfo> tables);

    void batchForUpdate(@Param("db_id") String id,
                        @Param("list") List<TableInfo> tables);
}
