package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.envisioniot.uscada.monitor.agent.exception.TaskException;
import com.envisioniot.uscada.monitor.agent.service.db.DbService;
import com.envisioniot.uscada.monitor.agent.service.monitor.AbstractMonitorService;
import com.envisioniot.uscada.monitor.common.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.*;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
@Service
@Slf4j
public class DbServiceImpl extends AbstractMonitorService<CommStat<DbStat>, List<DbStat>> {

    @Autowired
    private List<DbService> dbServiceList;

    @Autowired
    private DataSource dataSource;

    @Override
    public CommStat<DbStat> getMonitorSample(List<DbStat> dbInfos) {
        //TODO 20220114 agent对数据库的监测目前只支持MySQL
        if (CollectionUtils.isEmpty(dbInfos)) {
            log.warn("no db to monitor.");
            return null;
        }
        CommStat<DbStat> response = new CommStat<>();
        response.setHostIp(commonConfig.getLocalIp());
        List<DbStat> dbStats = new ArrayList<>(1);
        dbInfos.forEach(dbInfo -> {
            DbStat dbMonitor = getDbMonitor(dbInfo);
            dbStats.add(dbMonitor);
        });
        response.setData(dbStats);
        return response;
    }

    private DbStat getDbMonitor(DbStat db) {
        DbStat dbStat = new DbStat();
        BeanUtils.copyProperties(db, dbStat);
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbStat, true);
        closeDb(jdbcTemplate);
        return dbStat;
    }

    protected JdbcTemplate getJdbcTemplate(DbStat db, Boolean dbMonitor) {
        JdbcTemplate jdbcTemplate = null;
        DruidDataSource dataSource = null;
        db.setOccurTime(DateUtil.now());
        try {
            DbType dbType = DbType.getDB(db.getDbType());
            DbService dbService;
            for (DbService service : dbServiceList) {
                if (service.getDbType() == dbType) {
                    dbService = service;
                    dataSource = (DruidDataSource) dbService.getDataSource(db);
                    jdbcTemplate = new JdbcTemplate(dataSource);
                    if (dbMonitor) {
                        monitorDb(dbService, db, jdbcTemplate);
                    }
                    db.setStatus(ONLINE_STATUS);
                }
            }
        } catch (Exception e) {
            if (jdbcTemplate == null && dataSource != null) {
                dataSource.close();
            }
            String msg = "连接数据库错误: " + e.getMessage();
            db.setMsg(msg);
            db.setStatus(OFFLINE_STATUS);
            log.error(msg, e);
        }
        return jdbcTemplate;
    }

    private void monitorDb(DbService dbService, DbStat db, JdbcTemplate jdbcTemplate) {
        // db version
        db.setVersion(dbService.getVersion(jdbcTemplate));
        // db connection max
        db.setConnMax(dbService.getMaxConn(jdbcTemplate));
        // db used connections
        db.setUseCon(dbService.getUseCon(jdbcTemplate));
        // db Uptime
        db.setUpTime(dbService.getUptime(jdbcTemplate));
        // db questions
        db.setQuestions(dbService.getQuestions(jdbcTemplate));
        // db commit
        db.setCommitNum(dbService.getCommitNum(jdbcTemplate));
        // db rollback
        db.setRollbackNum(dbService.getRollbackNum(jdbcTemplate));
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getDb();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.DB_TASK.getUuid();
    }

    @Override
    public String samplePostUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + POST_DB_SAMPLE_URL;
    }

    @Override
    public String getMonitorObjUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + GET_DB_INFO_URL + "?hostIp=" + commonConfig.getLocalIp();
    }

    @Override
    public List<DbStat> convertObject(Object obj) {
        return JSONUtil.parse(obj).toBean(new TypeReference<List<DbStat>>() {
        });
    }

    /**
     * 默认监控${DB_HOST}
     */
    @Override
    public void initMonitor() {
//        try {
//            String dbHost = System.getenv("DB_HOST");
//            String isMysql = System.getenv("IS_MYSQL");
//            if ("1".equalsIgnoreCase(isMysql)) {
//                DruidDataSource ds = (DruidDataSource) this.dataSource;
//                DbStat dbStat = new DbStat();
//                dbStat.setDbIp(dbHost);
//                dbStat.setDbName(DEFAULT_DB_NAME);
//                dbStat.setDbType(DbType.MYSQL.getDbType());
//                dbStat.setDbPort(MYSQL_DEFAULT_PORT);
//                dbStat.setPasswd(ds.getPassword());
//                dbStat.setUser(ds.getUsername());
//                CommStat<DbStat> monitorSample = getMonitorSample(Collections.singletonList(dbStat));
//                pushInitMonitorObj(monitorSample);
//            } else {
//                log.error("just support mysql.");
//                // ignore
//            }
//        } catch (Exception e) {
//            log.error("init monitor task={} failed.", getTaskInfo());
//            log.error(e.getMessage(), e);
//        }
    }

    public boolean testDbConn(DbInfo db) {
        DruidDataSource ds = null;
        try {
            DbType dbType = DbType.getDB(db.getDbType());
            for (DbService service : dbServiceList) {
                if (service.getDbType() == dbType) {
                    ds = (DruidDataSource) service.getDataSource(db);
                    Connection connection = ds.getConnection(1000);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("test db conn fail. db={}", db);
            log.error(e.getMessage(), e);
            throw new TaskException(e.getMessage());
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
        return false;
    }

    public void closeDb(JdbcTemplate jdbcTemplate) {
        try {
            if (jdbcTemplate != null) {
                DruidDataSource ds = (DruidDataSource) jdbcTemplate.getDataSource();
                if (ds != null) {
                    ds.close();
                }
            }
        } catch (Exception e) {
            log.error("fail to close db={}", jdbcTemplate.getDataSource());
            log.error(e.getMessage(), e);
        }
    }
}
