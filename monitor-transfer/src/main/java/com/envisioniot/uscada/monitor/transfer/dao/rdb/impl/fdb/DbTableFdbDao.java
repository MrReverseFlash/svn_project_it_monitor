package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DbTableCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.DbTableFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbTableFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 14:03
 */
public class DbTableFdbDao extends DbTableCommDao {

    @Autowired
    private DbTableFWMapper dbTableFWMapper;

    @Override
    public void insertBatch(String id, List<TableInfo> tables) {
        dbTableFWMapper.insertBatch(id, tables);
    }

    @Override
    public void batchForUpdate(String id, List<TableInfo> tables) {
        dbTableFWMapper.batchForUpdate_update_first(id, tables);
        dbTableFWMapper.batchForUpdate_insert_later(id, tables);
    }
}
