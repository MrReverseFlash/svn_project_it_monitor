package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.IedStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.IedCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.IedMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: IedMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:41
 */
public class IedMysqlDao extends IedCommDao {

    @Autowired
    private IedMysqlMapper iedMysqlMapper;

    @Override
    public void insertBatch(String hostIp, List<IedStat> list) {
        iedMysqlMapper.insertBatch(hostIp, list);
    }

    @Override
    public void batchForUpdate(String hostIp, List<IedStat> list) {
        iedMysqlMapper.batchForUpdate(hostIp, list);
    }
}
