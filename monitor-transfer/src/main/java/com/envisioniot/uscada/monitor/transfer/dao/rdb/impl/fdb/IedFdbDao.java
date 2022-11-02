package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.IedStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.IedCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.IedFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: IedFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:42
 */
public class IedFdbDao extends IedCommDao {

    @Autowired
    private IedFWMapper iedFWMapper;

    @Override
    public void insertBatch(String hostIp, List<IedStat> list) {
        iedFWMapper.insertBatch(hostIp, list);
    }

    @Override
    public void batchForUpdate(String hostIp, List<IedStat> list) {
        iedFWMapper.batchForUpdate_update_first(hostIp, list);
        iedFWMapper.batchForUpdate_insert_later(hostIp, list);
    }
}
