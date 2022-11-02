package com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.web.dao.rdb.DbCommDao;
import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.entity.DbInfo;
import com.envisioniot.uscada.monitor.web.entity.SaveTableReq;
import com.envisioniot.uscada.monitor.web.mapper.fdb.read.DbFRMapper;
import com.envisioniot.uscada.monitor.web.mapper.fdb.write.DbFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 15:26
 */
public class DbFdbDao extends DbCommDao {

    @Autowired
    private DbFRMapper dbFRMapper;

    @Autowired
    private DbFWMapper dbFWMapper;

    @Override
    public int updateById(DbInfo dbInfo) {
        return dbFWMapper.updateById(dbInfo);
    }

    @Override
    public void save(DbInfo dbInfo) {
        dbFWMapper.save(dbInfo);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return dbFWMapper.deleteByIds(ids);
    }

    @Override
    public void saveTableInfo(SaveTableReq saveTableReq) {
        dbFWMapper.saveTableInfo(saveTableReq);
    }

    @Override
    public int updTableById(SaveTableReq saveTableReq) {
        return dbFWMapper.updTableById(saveTableReq);
    }

    @Override
    public int delTableByIds(List<Long> ids) {
        return dbFWMapper.delTableByIds(ids);
    }

    @Override
    public int delTableByDbIds(List<String> dbIds) {
        return dbFWMapper.delTableByDbIds(dbIds);
    }

    @Override
    public List<AlarmResponse> getDbAlarm(String st, String et, String dbId) {
        return dbFRMapper.getDbAlarm(st, et, dbId);
    }
}
