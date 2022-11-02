package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import com.envisioniot.uscada.monitor.agent.config.CommonConfig;
import com.envisioniot.uscada.monitor.common.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
@Service
@Slf4j
public class DbTableServiceImpl extends DbServiceImpl {

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public CommStat<DbStat> getMonitorSample(List<DbStat> request) {
        //TODO 20220114 agent部署在宿主机，无法直连FDB
        if (CollectionUtils.isEmpty(request)) {
            log.warn("no db to monitor.");
            return null;
        }
        CommStat<DbStat> response = new CommStat<>();
        response.setHostIp(commonConfig.getLocalIp());
        List<DbStat> dbStats = new ArrayList<>(1);
        request.forEach(dbState -> {
            JdbcTemplate jdbcTemplate = getJdbcTemplate(dbState, false);
            if (jdbcTemplate != null) {
                try {
                    DbStat dbStat = getTableCount(jdbcTemplate, dbState);
                    dbStats.add(dbStat);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    closeDb(jdbcTemplate);
                }
            }
        });
        response.setData(dbStats);
        return response;
    }

    private DbStat getTableCount(JdbcTemplate jdbcTemplate, DbStat dbStat) {
        List<TableInfo> tables = dbStat.getTables();
        if (CollectionUtils.isEmpty(tables)) {
            log.warn("db={} monitor tables is empty", dbStat);
            return dbStat;
        }
        tables.forEach((table -> {
            long num = queryTableCount(jdbcTemplate, table.getSql());
            table.setNum(num);
        }));
        return dbStat;
    }

    private long queryTableCount(JdbcTemplate jdbcTemplate, String sql) {
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            log.error("sql = {} execute failed.", sql);
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getTable();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.DB_TABLE_TASK.getUuid();
    }

    @Override
    public String samplePostUrl() {
        return super.samplePostUrl() + "?isTable=true";
    }

    @Override
    public String getMonitorObjUrl() {
        return super.getMonitorObjUrl() + "&isTable=true";
    }

    @Override
    public void initMonitor() {
        //ignore
    }
}
