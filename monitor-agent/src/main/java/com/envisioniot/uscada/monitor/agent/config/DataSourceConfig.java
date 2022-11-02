package com.envisioniot.uscada.monitor.agent.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.envision.scada.DBLogin;
import com.envision.scada.DBLoginException;
import com.envision.uscada.DbSsl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author hao.luo
 */
@Configuration
@Slf4j
public class DataSourceConfig {

    @Getter
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    @Primary
    @Bean("dataSource")
    public DataSource createDsInstance() {
        String dbType = System.getenv("IS_MYSQL");
        DruidDataSource ds = new DruidDataSource();
        if ("1".equals(dbType)) {
            ds.setUrl(getJdbcUrl());
            ds.setPassword(getPassword("os:envision"));
            ds.setLoginTimeout(3);
            ds.setConnectionErrorRetryAttempts(1);
            ds.setCreateScheduler(scheduler);
            ds.setDestroyScheduler(scheduler);
            ds.setAsyncCloseConnectionEnable(true);
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            ds.setUsername("envision");
            ds.setDbType("MySQL");
            ds.setInitialSize(5);
            ds.setMinIdle(5);
            ds.setMaxActive(10);
            ds.setMaxWait(10000);
            ds.setTimeBetweenEvictionRunsMillis(60000);
            ds.setMinEvictableIdleTimeMillis(300000);
            ds.setValidationQuery("SELECT 1");
            ds.setTestWhileIdle(true);
            ds.setTestOnBorrow(false);
            ds.setTestOnBorrow(false);
            ds.setMaxPoolPreparedStatementPerConnectionSize(5);
        } else if ("3".equals(dbType)) {
            String dbHostEnv = System.getenv("DB_HOST");
            if (StringUtils.isEmpty(dbHostEnv)) {
                dbHostEnv = "127.0.0.1";
            }
            String dbPort = System.getenv("DB_PORT");
            dbPort = dbPort == null ? "6535" : dbPort;
            String fdbUrl = "jdbc:sqlite-remote://" + dbHostEnv + ":" + dbPort + "/envision?user=envision&password=envision";
            String fdbDriverClassName = "com.envision.uscada.fdb.remote.FdbRemoteDriver";
            ds.setUrl(fdbUrl);
            ds.setDriverClassName(fdbDriverClassName);
            ds.setDbType("FDB");
            ds.setLoginTimeout(5);
            ds.setQueryTimeout(60);
            ds.setPhyTimeoutMillis(300000L);
            ds.setKillWhenSocketReadTimeout(true);
            ds.setInitialSize(1);
            ds.setMaxActive(10);
            ds.setMinIdle(1);
            ds.setValidationQuery("select count(1) num from fac_info");
            ds.setValidationQueryTimeout(5);
            ds.setTestWhileIdle(true);
            ds.setTestOnBorrow(false);
            ds.setTestOnReturn(false);
            ds.setTimeBetweenEvictionRunsMillis(600000L);
            ds.setMinEvictableIdleTimeMillis(1800000L);
            ds.setMaxWait(5000L);
            ds.setPoolPreparedStatements(false);
            ds.setMaxPoolPreparedStatementPerConnectionSize(-1);
            ds.setConnectionErrorRetryAttempts(3);
            ds.setNotFullTimeoutRetryCount(3);
            ds.setBreakAfterAcquireFailure(false);
            ds.setLogAbandoned(true);
            ds.setRemoveAbandoned(true);
            ds.setRemoveAbandonedTimeout(300);
        } else {
            log.warn("just support MySQL or FDB-Remote");
            ds.setMaxWait(1000);
            ds.setTestOnBorrow(true);
            ds.setValidationQuery("SELECT 1");
            ds.setValidationQueryTimeout(1);
        }
        return ds;
    }

    private static String getPassword(String dbUrl) {
        String password = "";
        try {
            password = DBLogin.getPassword(dbUrl);
        } catch (DBLoginException e) {
            Throwable ce = e.getCause();
            String errMsg = String.format("获取数据库登录密码失败！%s[%s]", e.getMessage(), ce.getMessage());
            log.error(errMsg);
        }
        return StringUtils.isEmpty(password) ? "envision" : password;
    }

    private String getJdbcUrl() {
//        String ipMaster = "172.20.100.74";
        String ipMaster = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        dbPort = dbPort == null ? "3306" : dbPort;
        String jdbcUrl = "jdbc:mysql://" + ipMaster + ":" + dbPort + "/envision?useUnicode=true&failOverReadOnly=false&autoReconnect=true&characterEncoding=utf-8";
        return DbSsl.getSslUrl(jdbcUrl);
    }
}
