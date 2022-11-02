package com.envisioniot.uscada.monitor.web.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.web.dao.rdb.PortCommDao;
import com.envisioniot.uscada.monitor.web.entity.PortInfo;
import com.envisioniot.uscada.monitor.web.mapper.mysql.PortMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: PortMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 14:35
 */
public class PortMysqlDao extends PortCommDao {

    @Autowired
    private PortMysqlMapper portMysqlMapper;

    @Override
    public int updateById(PortInfo portInfo) {
        return portMysqlMapper.updateById(portInfo);
    }

    @Override
    public void save(PortInfo portInfo) {
        portMysqlMapper.save(portInfo);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return portMysqlMapper.deleteByIds(ids);
    }
}
