package com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.web.dao.rdb.DashBoardCommDao;
import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.entity.HostTable;
import com.envisioniot.uscada.monitor.web.mapper.fdb.read.DashBoardFRMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DashBoardFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 14:27
 */
public class DashBoardFdbDao extends DashBoardCommDao {

    @Autowired
    private DashBoardFRMapper dashBoardFRMapper;

    @Override
    public List<HostTable> qryCpuTmpTop10() {
        return dashBoardFRMapper.qryCpuTmpTop10();
    }

    @Override
    public List<HostTable> qryCpuPerTop10() {
        return dashBoardFRMapper.qryCpuPerTop10();
    }

    @Override
    public List<HostTable> qryMemPerTop10() {
        return dashBoardFRMapper.qryMemPerTop10();
    }

    @Override
    public List<HostTable> qryDiskPerTop10() {
        return dashBoardFRMapper.qryDiskPerTop10();
    }

    @Override
    public List<HostTable> qryNetPerTop10() {
        return dashBoardFRMapper.qryNetPerTop10();
    }

    @Override
    public List<AlarmResponse> getAllAlarm() {
        return dashBoardFRMapper.getAllAlarm();
    }
}
