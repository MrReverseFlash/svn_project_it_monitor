package com.envisioniot.uscada.monitor.web.mapper.comm;

import com.envisioniot.uscada.monitor.web.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbCommMapper {

    List<DbInfo> qryDbList(@Param("hostIp") String hostIp);

    List<TableInfo> qryAllTable();

    List<DbSampleReq> qryDbSampleReqByIds(List<String> ids);

    List<TableSampleReq> qryTableSampleReqByIds(List<Long> ids);

    List<TableSampleReq> qryTableSampleReqByDbIds(List<String> dbIds);

}
