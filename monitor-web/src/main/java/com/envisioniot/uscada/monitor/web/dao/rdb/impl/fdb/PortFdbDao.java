package com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.web.dao.rdb.PortCommDao;
import com.envisioniot.uscada.monitor.web.entity.PortInfo;
import com.envisioniot.uscada.monitor.web.mapper.fdb.write.PortFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: PortFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 14:49
 */
public class PortFdbDao extends PortCommDao {

    @Autowired
    private PortFWMapper portFWMapper;

    @Override
    public int updateById(PortInfo portInfo) {
        return portFWMapper.updateById(portInfo);
    }

    @Override
    public void save(PortInfo portInfo) {
        portFWMapper.save(portInfo);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return portFWMapper.deleteByIds(ids);
    }
}
