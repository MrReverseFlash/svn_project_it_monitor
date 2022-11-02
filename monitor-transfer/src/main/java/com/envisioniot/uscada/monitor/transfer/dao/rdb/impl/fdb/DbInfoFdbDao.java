package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.DbStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DbInfoCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.read.DbInfoFRMapper;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.DbInfoFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbInfoFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/12 21:30
 */
public class DbInfoFdbDao extends DbInfoCommDao {

    @Autowired
    private DbInfoFRMapper dbInfoFRMapper;

    @Autowired
    private DbInfoFWMapper dbInfoFWMapper;

    @Override
    public void batchForUpdate(String hostIp, List<DbStat> sampleData) {
        dbInfoFWMapper.batchForUpdate_update_first(hostIp, sampleData);
        dbInfoFWMapper.batchForUpdate_insert_later(hostIp, sampleData);
    }

    @Override
    public void insertBatch(String hostIp, List<DbStat> dbInfo) {
        dbInfoFWMapper.insertBatch(hostIp, dbInfo);
    }

    @Override
    public void insert(String hostIp, DbStat db) {
        dbInfoFWMapper.insert_insert_first(hostIp, db);
    }

    @Override
    public String selectIdByInfo(String hostIp, DbStat db) {
        return dbInfoFWMapper.selectIdByInfo(hostIp, db);
    }

    @Override
    public String qryDbAlarmObjById(String dbId) {
        return dbInfoFRMapper.qryDbAlarmObjById(dbId);
    }
}
