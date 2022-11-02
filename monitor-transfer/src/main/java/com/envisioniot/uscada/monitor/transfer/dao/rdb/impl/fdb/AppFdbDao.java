package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.AppStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.AppCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.AppFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: AppFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/12 11:37
 */
public class AppFdbDao extends AppCommDao {

    @Autowired
    private AppFWMapper appFWMapper;

    @Override
    public void update(String hostIp, AppStat app) {
        int updNum = appFWMapper.update_update_first(hostIp, app);
        if (updNum <= 0) {
            appFWMapper.update_insert_iffail(hostIp, app);
        }
    }

    @Override
    public void insert(String hostIp, List<AppStat> app) {
        appFWMapper.insert(hostIp, app);
    }

    @Override
    public void batchForUpdate(String hostIp, List<AppStat> sampleData) {
        appFWMapper.batchForUpdate_update_first(hostIp, sampleData);
        appFWMapper.batchForUpdate_insert_later(hostIp, sampleData);
    }

    @Override
    public void updateBatch(String hostIp, List<AppStat> sampleData) {
        appFWMapper.updateBatch(hostIp, sampleData);
    }
}
