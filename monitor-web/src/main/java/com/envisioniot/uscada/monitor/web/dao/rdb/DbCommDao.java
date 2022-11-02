package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.DbInfo;
import com.envisioniot.uscada.monitor.web.entity.DbSampleReq;
import com.envisioniot.uscada.monitor.web.entity.TableInfo;
import com.envisioniot.uscada.monitor.web.entity.TableSampleReq;
import com.envisioniot.uscada.monitor.web.mapper.comm.DbCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DbCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 15:21
 */
public abstract class DbCommDao implements DbDao{

    @Autowired
    protected DbCommMapper dbCommMapper;

    @Override
    public List<DbInfo> qryDbList(String hostIp) {
        return dbCommMapper.qryDbList(hostIp);
    }

    @Override
    public List<TableInfo> qryAllTable() {
        return dbCommMapper.qryAllTable();
    }

    @Override
    public List<DbSampleReq> qryDbSampleReqByIds(List<String> ids) {
        return dbCommMapper.qryDbSampleReqByIds(ids);
    }

    @Override
    public List<TableSampleReq> qryTableSampleReqByIds(List<Long> ids) {
        return dbCommMapper.qryTableSampleReqByIds(ids);
    }

    @Override
    public List<TableSampleReq> qryTableSampleReqByDbIds(List<String> dbIds) {
        return dbCommMapper.qryTableSampleReqByDbIds(dbIds);
    }
}
