package com.envisioniot.uscada.monitor.transfer.mapper.fdb.write;

import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DbTableFWMapper {

    void insertBatch(@Param("db_id") String id,
                     @Param("list") List<TableInfo> tables);

    void batchForUpdate_update_first(@Param("db_id") String id,
                                     @Param("list") List<TableInfo> tables);

    void batchForUpdate_insert_later(@Param("db_id") String id,
                                     @Param("list") List<TableInfo> tables);
}
