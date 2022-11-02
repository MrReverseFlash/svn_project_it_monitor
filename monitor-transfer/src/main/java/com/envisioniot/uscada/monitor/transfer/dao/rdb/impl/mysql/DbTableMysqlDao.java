package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DbTableCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.DbTableMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbTableMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 14:01
 */
public class DbTableMysqlDao extends DbTableCommDao {

    @Autowired
    private DbTableMysqlMapper dbTableMysqlMapper;

    @Override
    public void insertBatch(String id, List<TableInfo> tables) {
        dbTableMysqlMapper.insertBatch(id, tables);
    }

    @Override
    public void batchForUpdate(String id, List<TableInfo> tables) {
        dbTableMysqlMapper.batchForUpdate(id, tables);
    }
}
