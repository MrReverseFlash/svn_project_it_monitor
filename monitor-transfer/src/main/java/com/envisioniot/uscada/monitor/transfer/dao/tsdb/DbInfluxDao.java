package com.envisioniot.uscada.monitor.transfer.dao.tsdb;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.envisioniot.uscada.monitor.common.entity.DbStat;
import com.envisioniot.uscada.monitor.common.entity.TableInfo;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.envisioniot.uscada.monitor.common.util.InfluxConstants.*;

/**
 * @author hao.luo
 * @date 2020-12-28
 */
@Repository
@Slf4j
public class DbInfluxDao {

    @Autowired
    private InfluxDB influxdb;

    public void insertDbHis(String hostIp, List<DbStat> sampleData) {
        try {
            sampleData.forEach((db -> {
                String occurTime = db.getOccurTime();
                long timestamp = DateUtil.parse(occurTime, CommConstants.DEFAULT_TIME_FORMAT).getTime();
                Point.Builder builder = Point.measurement(INFLUX_DB_HIS)
                        .time(timestamp, TimeUnit.MILLISECONDS)
                        .tag(TAG_IP, hostIp)
                        .tag(TAG_DB_IP, db.getDbIp())
                        .tag(TAG_DB_TYPE, db.getDbType())
                        .tag(TAG_DB_NAME, db.getDbName())
                        .addField(FIELD_STATUS, db.getStatus());
                if (db.getStatus() == CommConstants.ONLINE_STATUS) {
                    builder.addField(FIELD_QUESTIONS, db.getQuestions())
                            .addField(FIELD_COMMIT, db.getCommitNum())
                            .addField(FIELD_ROLLBACK, db.getRollbackNum())
                            .addField(FIELD_CONNMAX, db.getConnMax())
                            .addField(FIELD_USECON, db.getUseCon());
                }
                Point point = builder.build();
                influxdb.write(point);
            }));
        } catch (Exception e) {
            log.error("host_ip={} db info sample save in influxdb error.", hostIp);
            log.error(e.getMessage(), e);
        }
    }

    public void insertTableHis(DbStat db, String hostIp) {
        try {
//            Integer status = db.getStatus();
//            if (status == null || status != CommConstants.ONLINE_STATUS) {
//                return;
//            }
            List<TableInfo> tables = db.getTables();
            if (CollectionUtil.isEmpty(tables)) {
                log.warn("host ip ={}, db={} db table sample is empty.", hostIp, db.getDbIp());
                return;
            }
            String occurTime = db.getOccurTime();
            long timestamp = DateUtil.parse(occurTime, CommConstants.DEFAULT_TIME_FORMAT).getTime();
            tables.forEach((table -> {
                Point point = Point.measurement(INFLUX_TABLE_HIS)
                        .time(timestamp, TimeUnit.MILLISECONDS)
                        .tag(TAG_IP, hostIp)
                        .tag(TAG_DB_IP, db.getDbIp())
                        .tag(TAG_DB_TYPE, db.getDbType())
                        .tag(TAG_DB_NAME, db.getDbName())
                        .tag(TAG_TABLE_NAME, table.getTableName())
                        .addField(FIELD_TABLE_NUM, table.getNum())
                        .build();
                influxdb.write(point);
            }));
        } catch (Exception e) {
            log.error("host_ip={} db table sample save in influxdb error.", hostIp);
            log.error(e.getMessage(), e);
        }
    }

}
