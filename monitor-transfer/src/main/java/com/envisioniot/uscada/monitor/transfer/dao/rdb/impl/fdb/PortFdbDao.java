package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.PortStatSample;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.PortCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.PortFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: PortFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 17:09
 */
public class PortFdbDao extends PortCommDao {

    @Autowired
    private PortFWMapper portFWMapper;

    @Override
    public void insertBatch(String hostIp, List<PortStatSample> list) {
        portFWMapper.insertBatch(hostIp, list);
    }

    @Override
    public void batchForUpdate(String hostIp, List<PortStatSample> list) {
        portFWMapper.batchForUpdate_update_first(hostIp, list);
        portFWMapper.batchForUpdate_insert_later(hostIp, list);
    }
}
