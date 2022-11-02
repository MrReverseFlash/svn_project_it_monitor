package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.mapper.comm.DashBoardCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: DashBoardCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 14:21
 */
public abstract class DashBoardCommDao implements DashBoardDao{

    @Autowired
    protected DashBoardCommMapper dashBoardCommMapper;

    /**
     * 查询主机数
     *
     * @return
     */
    @Override
    public int qryHostNum() {
        return dashBoardCommMapper.qryHostNum();
    }

    /**
     * 查询数据库数
     * @return
     */
    @Override
    public int qryDbNum() {
        return dashBoardCommMapper.qryDbNum();
    }

    /**
     * 查询进程数
     * @return
     */
    @Override
    public int qryAppNum() {
        return dashBoardCommMapper.qryAppNum();
    }

    /**
     * 查询端口数
     * @return
     */
    @Override
    public int qryPortNum() {
        return dashBoardCommMapper.qryPortNum();
    }

    /**
     * 查询主机在线数
     * @return
     */
    @Override
    public int qryOnLineNum() {
        return dashBoardCommMapper.qryOnLineNum();
    }

    /**
     * 查询主机离线数
     * @return
     */
    @Override
    public int qryOffLineNum(){
        return dashBoardCommMapper.qryOffLineNum();
    }

    /**
     * 查询所有scada状态
     * @return
     */
    @Override
    public List<String> qryAllScadaStat() {
        return dashBoardCommMapper.qryAllScadaStat();
    }
}
