package com.envisioniot.uscada.monitor.transfer.service;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.common.entity.DbStat;
import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DbInfoDao;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.DbTableDao;
import com.envisioniot.uscada.monitor.transfer.dao.tsdb.DbInfluxDao;
import com.envisioniot.uscada.monitor.transfer.exception.DataProcessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author hao.luo
 * @date 2020-12-28
 */
@Service
@Slf4j
public class DbService {

    @Autowired
    private DbInfluxDao dbInfluxDao;

    @Autowired
    private DbInfoDao dbInfoDao;

    @Autowired
    private DbTableDao dbTableDao;

    public void saveDbSample(CommStat<DbStat> sample, boolean isInit) {
        String hostIp = sample.getHostIp();
        try {
            List<DbStat> sampleData = sample.getData();
            if (CollectionUtil.isEmpty(sampleData)) {
                log.warn("host ip = {} db info sample is empty.", hostIp);
                return;
            }
            sampleData.forEach(dbStat -> dbStat.setId(UUID.randomUUID().toString()));
            if (isInit) {
                dbInfoDao.insertBatch(hostIp, sampleData);
            } else {
                dbInfoDao.batchForUpdate(hostIp, sampleData);
                dbInfluxDao.insertDbHis(hostIp, sampleData);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} db info sample data save fail.", hostIp));
        }
    }

    public void saveTableSample(CommStat<DbStat> sample, boolean isInit) {
        String hostIp = sample.getHostIp();
        try {
            List<DbStat> sampleData = sample.getData();
            if (CollectionUtil.isEmpty(sampleData)) {
                log.warn("host ip = {} db info sample is empty.", hostIp);
                return;
            }
            sampleData.forEach((db) -> {
                db.setId(UUID.randomUUID().toString());
                // 如果不存在db则插入
                dbInfoDao.insert(hostIp, db);
                db.setId(dbInfoDao.selectIdByInfo(hostIp, db));
                List<TableInfo> tables = db.getTables();
                if (CollectionUtil.isEmpty(tables)) {
                    log.warn("db={} has no monitor table", db);
                    return;
                }
                if (isInit) {
                    // 判断默认对象是否在监控列表中，如果不存在则插入，hdb中记录不删除只更新
                    dbTableDao.insertBatch(db.getId(), tables);
                } else {
                    dbTableDao.batchForUpdate(db.getId(), tables);
                    // 插入采样信息到influxdb
                    dbInfluxDao.insertTableHis(db, hostIp);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} db info sample data save fail.", hostIp));
        }
    }

    public List<DbStat> getMonitorObj(String hostIp, Boolean isTable) {
        try{
            List<DbStat> monitorDb = dbInfoDao.getMonitorDb(hostIp);
            if (CollectionUtil.isEmpty(monitorDb)) {
                log.error("ip={} has no monitor db.", hostIp);
                return null;
            }
            if (isTable) {
                monitorDb.forEach((db) -> {
                    List<TableInfo> tables = dbTableDao.getMonitorTable(db.getId());
                    db.setTables(tables);
                });
            }
            return monitorDb;
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new DataProcessException(String.format("agent ip={%s} get monitor obj fail.", hostIp));
        }
    }
}
