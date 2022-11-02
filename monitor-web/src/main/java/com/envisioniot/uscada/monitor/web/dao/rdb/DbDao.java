package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.*;

import java.util.List;

/**
 * <p>Title: DbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 15:20
 */
public interface DbDao {

    List<DbInfo> qryDbList(String hostIp);

    int updateById(DbInfo dbInfo);

    void save(DbInfo dbInfo);

    int deleteByIds(List<String> ids);

    List<TableInfo> qryAllTable();

    void saveTableInfo(SaveTableReq saveTableReq);

    int updTableById(SaveTableReq saveTableReq);

    int delTableByIds(List<Long> ids);

    int delTableByDbIds(List<String> dbIds);

    List<DbSampleReq> qryDbSampleReqByIds(List<String> ids);

    List<TableSampleReq> qryTableSampleReqByIds(List<Long> ids);

    List<TableSampleReq> qryTableSampleReqByDbIds(List<String> dbIds);

    List<AlarmResponse> getDbAlarm(String st, String et, String dbId);
}
