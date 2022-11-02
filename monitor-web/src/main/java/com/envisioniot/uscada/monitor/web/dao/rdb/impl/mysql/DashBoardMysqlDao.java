package com.envisioniot.uscada.monitor.web.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.web.dao.rdb.DashBoardCommDao;
import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.entity.HostTable;
import com.envisioniot.uscada.monitor.web.mapper.mysql.DashBoardMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DashBoardMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 14:25
 */
public class DashBoardMysqlDao extends DashBoardCommDao {

    @Autowired
    private DashBoardMysqlMapper dashBoardMysqlMapper;

    @Override
    public List<HostTable> qryCpuTmpTop10() {
        return dashBoardMysqlMapper.qryCpuTmpTop10();
    }

    @Override
    public List<HostTable> qryCpuPerTop10() {
        return dashBoardMysqlMapper.qryCpuPerTop10();
    }

    @Override
    public List<HostTable> qryMemPerTop10() {
        return dashBoardMysqlMapper.qryMemPerTop10();
    }

    @Override
    public List<HostTable> qryDiskPerTop10() {
        return dashBoardMysqlMapper.qryDiskPerTop10();
    }

    @Override
    public List<HostTable> qryNetPerTop10() {
        return dashBoardMysqlMapper.qryNetPerTop10();
    }

    @Override
    public List<AlarmResponse> getAllAlarm() {
        return dashBoardMysqlMapper.getAllAlarm();
    }
}
