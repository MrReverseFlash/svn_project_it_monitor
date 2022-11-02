package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.FeInfo;
import com.envisioniot.uscada.monitor.web.mapper.comm.FeCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: FeCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 15:57
 */
public abstract class FeCommDao implements FeDao{

    @Autowired
    protected FeCommMapper feCommMapper;

    @Override
    public List<FeInfo> qryAll() {
        return feCommMapper.qryAll();
    }
}
