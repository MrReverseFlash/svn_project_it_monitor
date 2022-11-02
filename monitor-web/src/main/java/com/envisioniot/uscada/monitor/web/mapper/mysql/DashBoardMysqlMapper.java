package com.envisioniot.uscada.monitor.web.mapper.mysql;

import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.entity.HostTable;

import java.util.List;

/**
 * DashBoardMysqlMapper
 *
 * @author yangkang
 * @date 2021/3/3
 */
public interface DashBoardMysqlMapper {

    /**
     * 查询近1分钟的CPU温度前10
     * @return
     */
    List<HostTable> qryCpuTmpTop10();

    /**
     * 查询近1分钟的CPU占用前10
     * @return
     */
    List<HostTable> qryCpuPerTop10();

    /**
     * 查询近1分钟的内存占用前10
     * @return
     */
    List<HostTable> qryMemPerTop10();

    /**
     * 查询近1分钟的硬盘占用前10
     * @return
     */
    List<HostTable> qryDiskPerTop10();

    /**
     * 查询近1分钟的网络IO占用前10
     * @return
     */
    List<HostTable> qryNetPerTop10();

    /**
     * 获取当日的所有告警
     * @return
     */
    List<AlarmResponse> getAllAlarm();
}
