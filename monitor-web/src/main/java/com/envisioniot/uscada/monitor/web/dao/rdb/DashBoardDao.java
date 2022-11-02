package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.entity.HostTable;

import java.util.List;

/**
 * <p>Title: DashBoardDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 14:19
 */
public interface DashBoardDao {

    /**
     * 查询主机数
     * @return
     */
    int qryHostNum();

    /**
     * 查询数据库数
     * @return
     */
    int qryDbNum();

    /**
     * 查询进程数
     * @return
     */
    int qryAppNum();

    /**
     * 查询端口数
     * @return
     */
    int qryPortNum();

    /**
     * 查询主机在线数
     * @return
     */
    int qryOnLineNum();

    /**
     * 查询主机离线数
     * @return
     */
    int qryOffLineNum();

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

    /**
     * 获取所有监控的scada状态
     * @return
     */
    List<String> qryAllScadaStat();
}
