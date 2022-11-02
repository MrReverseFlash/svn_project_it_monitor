package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.AppStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.AppCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.AppMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: AppMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/12 11:35
 */
public class AppMysqlDao extends AppCommDao {

    @Autowired
    private AppMysqlMapper appMysqlMapper;

    @Override
    public void update(String hostIp, AppStat app) {
        appMysqlMapper.update(hostIp, app);
    }

    @Override
    public void insert(String hostIp, List<AppStat> app) {
        appMysqlMapper.insert(hostIp, app);
    }

    @Override
    public void batchForUpdate(String hostIp, List<AppStat> sampleData) {
        appMysqlMapper.batchForUpdate(hostIp, sampleData);
    }

    @Override
    public void updateBatch(String hostIp, List<AppStat> sampleData) {
        appMysqlMapper.updateBatch(hostIp, sampleData);
    }
}
