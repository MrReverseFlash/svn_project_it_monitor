package com.envisioniot.uscada.monitor.web.mapper.comm;

import java.util.List;

/**
 * DashBoardCommMapper
 *
 * @author yangkang
 * @date 2021/3/3
 */
public interface DashBoardCommMapper {

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
     * 获取所有监控的scada状态
     * @return
     */
    List<String> qryAllScadaStat();
}
