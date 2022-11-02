package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import com.envisioniot.uscada.monitor.transfer.mapper.comm.DbTableCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbTableCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 13:59
 */
public abstract class DbTableCommDao implements DbTableDao{

    @Autowired
    protected DbTableCommMapper dbTableCommMapper;

    @Override
    public List<TableInfo> getMonitorTable(String id) {
        return dbTableCommMapper.getMonitorTable(id);
    }
}
