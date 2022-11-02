package com.envisioniot.uscada.monitor.web.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.web.dao.rdb.DbCommDao;
import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.entity.DbInfo;
import com.envisioniot.uscada.monitor.web.entity.SaveTableReq;
import com.envisioniot.uscada.monitor.web.mapper.mysql.DbMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 15:24
 */
public class DbMysqlDao extends DbCommDao {

    @Autowired
    private DbMysqlMapper dbMysqlMapper;

    @Override
    public int updateById(DbInfo dbInfo) {
        return dbMysqlMapper.updateById(dbInfo);
    }

    @Override
    public void save(DbInfo dbInfo) {
        dbMysqlMapper.save(dbInfo);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return dbMysqlMapper.deleteByIds(ids);
    }

    @Override
    public void saveTableInfo(SaveTableReq saveTableReq) {
        dbMysqlMapper.saveTableInfo(saveTableReq);
    }

    @Override
    public int updTableById(SaveTableReq saveTableReq) {
        return dbMysqlMapper.updTableById(saveTableReq);
    }

    @Override
    public int delTableByIds(List<Long> ids) {
        return dbMysqlMapper.delTableByIds(ids);
    }

    @Override
    public int delTableByDbIds(List<String> dbIds) {
        return dbMysqlMapper.delTableByDbIds(dbIds);
    }

    @Override
    public List<AlarmResponse> getDbAlarm(String st, String et, String dbId) {
        return dbMysqlMapper.getDbAlarm(st, et, dbId);
    }
}
