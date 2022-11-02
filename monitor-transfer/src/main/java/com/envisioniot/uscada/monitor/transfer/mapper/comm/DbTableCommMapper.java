package com.envisioniot.uscada.monitor.transfer.mapper.comm;

import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DbTableCommMapper {

    List<TableInfo> getMonitorTable(@Param("db_id") String id);
}
