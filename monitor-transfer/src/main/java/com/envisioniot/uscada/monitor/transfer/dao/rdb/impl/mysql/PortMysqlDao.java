package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.PortStatSample;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.PortCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.PortMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: PortMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:57
 */
public class PortMysqlDao extends PortCommDao {

    @Autowired
    private PortMysqlMapper portMysqlMapper;

    @Override
    public void insertBatch(String hostIp, List<PortStatSample> list) {
        portMysqlMapper.insertBatch(hostIp, list);
    }

    @Override
    public void batchForUpdate(String hostIp, List<PortStatSample> list) {
        portMysqlMapper.batchForUpdate(hostIp, list);
    }
}
