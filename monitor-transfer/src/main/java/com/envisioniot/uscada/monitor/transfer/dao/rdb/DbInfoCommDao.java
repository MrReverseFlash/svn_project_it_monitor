package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.common.entity.DbStat;
import com.envisioniot.uscada.monitor.transfer.mapper.comm.DbInfoCommMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbInfoCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/12 21:24
 */
public abstract class DbInfoCommDao implements DbInfoDao{

    @Autowired
    private DbInfoCommMapper dbInfoCommMapper;

    @Override
    public List<DbStat> getMonitorDb(String hostIp) {
        return dbInfoCommMapper.getMonitorDb(hostIp);
    }

}
