package com.envisioniot.uscada.monitor.agent.mapper;

import com.envisioniot.uscada.monitor.common.entity.IedStat;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-24
 */
@Mapper
@Repository
public interface ScadaMapper {

    /**
     * @return 获取所有ied信息
     */
    public List<IedStat> getAllIedInfo();

    List<Integer> getDistinctHostIp();

}
