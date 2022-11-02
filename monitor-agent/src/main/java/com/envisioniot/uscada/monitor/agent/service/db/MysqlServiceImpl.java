package com.envisioniot.uscada.monitor.agent.service.db;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.envisioniot.uscada.monitor.agent.component.RDSConnection;
import com.envisioniot.uscada.monitor.agent.config.DataSourceConfig;
import com.envisioniot.uscada.monitor.common.entity.DbInfo;
import com.envisioniot.uscada.monitor.common.entity.DbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;

import static com.envisioniot.uscada.monitor.agent.util.Constants.DEFAULT_PRECISION;
import static com.envisioniot.uscada.monitor.agent.component.RDSConnection.*;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
@Service
public class MysqlServiceImpl implements DbService {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Override
    public DataSource getDataSource(DbInfo db) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        String url = RDSConnection.url_mysql;
        url = url.replace("{ip}", db.getDbIp()).replace("{port}", db.getDbPort()).replace("{dbname}", db.getDbName());
        dataSource.setDriverClassName(RDSConnection.driver_mysql);
        dataSource.setUrl(url);
        dataSource.setUsername(db.getUser());
        dataSource.setPassword(db.getPasswd());
        dataSource.setMaxActive(2);
        dataSource.setMaxWait(5000);
        dataSource.setTestWhileIdle(true);
        dataSource.setFailFast(true);
        dataSource.setTimeBetweenEvictionRunsMillis(3000);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setTimeBetweenConnectErrorMillis(200);
        dataSource.setValidationQuery("select 1");
        dataSource.setValidationQueryTimeout(2);
//        dataSource.setFilters("stat,wall,slf4j");
//        dataSource.setUseGlobalDataSourceStat(true);
        ScheduledExecutorService scheduler = dataSourceConfig.getScheduler();
        dataSource.setCreateScheduler(scheduler);
        dataSource.setDestroyScheduler(scheduler);
        dataSource.setAsyncCloseConnectionEnable(true);
        dataSource.setConnectionErrorRetryAttempts(1);
        dataSource.init();
        return dataSource;
    }

    @Override
    public DbType getDbType() {
        return DbType.MYSQL;
    }

    @Override
    public String getVersion(JdbcTemplate jdbcTemplate) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(MYSQL_VERSION);
        if (sqlRowSet.next()) {
            return sqlRowSet.getString(1);
        }
        return null;
    }

    @Override
    public Integer getMaxConn(JdbcTemplate jdbcTemplate) {
        final Integer[] result = new Integer[1];
        jdbcTemplate.query(MYSQL_MAX_CONN, resultSet -> {
            result[0] = resultSet.getInt(2);
        });
        return result[0];
    }

    @Override
    public Integer getUseCon(JdbcTemplate jdbcTemplate) {
        final Integer[] result = new Integer[1];
        jdbcTemplate.query(MYSQL_USE_CONN, resultSet -> {
            result[0] = resultSet.getInt(2);
        });
        return result[0];
    }

    @Override
    public Long getUptime(JdbcTemplate jdbcTemplate) {
        final Long[] result = new Long[1];
        jdbcTemplate.query(MYSQL_UPTIME, resultSet -> {
            result[0] = resultSet.getLong(2);
        });
        return result[0];
    }

    @Override
    public Long getQuestions(JdbcTemplate jdbcTemplate) {
        final Long[] result = new Long[1];
        jdbcTemplate.query(MYSQL_QUESTIONS, resultSet -> {
            result[0] = resultSet.getLong(2);
        });
        return result[0];
    }

    @Override
    public Long getCommitNum(JdbcTemplate jdbcTemplate) {
        final Long[] result = new Long[1];
        jdbcTemplate.query(MYSQL_COMMIT, resultSet -> {
            result[0] = resultSet.getLong(2);
        });
        return result[0];
    }

    @Override
    public Long getRollbackNum(JdbcTemplate jdbcTemplate) {
        final Long[] result = new Long[1];
        jdbcTemplate.query(MYSQL_ROLLBACK, resultSet -> {
            result[0] = resultSet.getLong(2);
        });
        return result[0];
    }

    public Double getQps(JdbcTemplate jdbcTemplate) {
        final Long[] result = new Long[2];
        jdbcTemplate.query(MYSQL_QUESTIONS, resultSet -> {
            result[0] = resultSet.getLong(2);
        });
        long questions = result[0];
        jdbcTemplate.query(MYSQL_UPTIME, resultSet -> {
            result[1] = resultSet.getLong(2);
        });
        long upTime = result[1];
        if (upTime != 0L) {
            return NumberUtil.div((float) questions, (float) upTime, DEFAULT_PRECISION);
        }
        return null;
    }

    public Double getTps(JdbcTemplate jdbcTemplate) {
        final Long[] result = new Long[2];
        jdbcTemplate.query(MYSQL_COMMIT, resultSet -> {
            result[0] = resultSet.getLong(2);
        });
        long questions = result[0];
        jdbcTemplate.query(MYSQL_UPTIME, resultSet -> {
            result[0] = resultSet.getLong(2);
        });
        long upTime = result[1];
        if (upTime != 0L) {
            return NumberUtil.div((float) questions, (float) upTime, DEFAULT_PRECISION);
        }
        return null;
    }
}
