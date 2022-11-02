package com.envisioniot.uscada.monitor.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.envision.scada.DBLogin;
import com.envision.scada.DBLoginException;
import com.envision.uscada.DbSsl;
import com.envisioniot.uscada.monitor.common.enums.DBType;
import com.envisioniot.uscada.monitor.common.util.CommUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Properties;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.*;

/**
 * ReadDsCfg
 *
 * @author yangkang
 * @date 2021/5/26
 */
@Slf4j
@Configuration
@MapperScan(basePackages = {"com.envisioniot.uscada.monitor.web.mapper.mysql",
                            "com.envisioniot.uscada.monitor.web.mapper.fdb.read",
                            "com.envisioniot.uscada.monitor.web.mapper.comm"},
            sqlSessionFactoryRef = "sqlSessionFactory")
public class ReadDsCfg {

    private DruidDataSource ddsR;

    @PreDestroy
    public void destroy(){
        if (ddsR != null && !ddsR.isClosed()) {
            ddsR.setRemoveAbandonedTimeout(0);
            ddsR.removeAbandoned();
            ddsR.close();
        }
    }

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource dataSourceR(){
        if (CommUtil.getDB_TYPE() == DBType.FDB) {
            String fdbUrl;
            String fdbDriverClassName;
            ArrayList<String> initSqlsList = new ArrayList<>(1);
            String dbHostEnv = System.getenv("DB_HOST");
            if (StringUtils.isEmpty(dbHostEnv)) {
                dbHostEnv = "127.0.0.1";
            }
            String dbPort = System.getenv("DB_PORT");
            dbPort = dbPort == null ? "6535" : dbPort;
            fdbUrl = "jdbc:sqlite-remote://" + dbHostEnv + ":" + dbPort + "/envision?user=envision&password=envision";
            fdbDriverClassName = "com.envision.uscada.fdb.remote.FdbRemoteDriver";
            try {
                //select从读进程池获取
                ddsR = new DruidDataSource();
                ddsR.setUrl(fdbUrl);
                ddsR.setDriverClassName(fdbDriverClassName);
                ddsR.setDbType("FDB");
                ddsR.setLoginTimeout(5);
                ddsR.setQueryTimeout(60);
                ddsR.setPhyTimeoutMillis(300000L);
                ddsR.setKillWhenSocketReadTimeout(true);
                ddsR.setInitialSize(1);
                ddsR.setMaxActive(3);
                ddsR.setMinIdle(1);
                ddsR.setValidationQuery("select count(1) num from fac_info");
                ddsR.setValidationQueryTimeout(5);
                ddsR.setTestWhileIdle(true);
                ddsR.setTestOnBorrow(false);
                ddsR.setTestOnReturn(false);
                ddsR.setTimeBetweenEvictionRunsMillis(600000L);
                ddsR.setMinEvictableIdleTimeMillis(1800000L);
                ddsR.setMaxWait(5000L);
                ddsR.setPoolPreparedStatements(false);
                ddsR.setMaxPoolPreparedStatementPerConnectionSize(-1);
                ddsR.setConnectionErrorRetryAttempts(3);
                ddsR.setNotFullTimeoutRetryCount(3);
                ddsR.setBreakAfterAcquireFailure(false);
                ddsR.setLogAbandoned(true);
                ddsR.setRemoveAbandoned(true);
                ddsR.setRemoveAbandonedTimeout(300);
                ddsR.setAsyncInit(true);
                ddsR.setConnectionInitSqls(initSqlsList);
                ddsR.init();
                return ddsR;
            } catch (Exception e) {
                log.error("GET_FDBCONN_ERR", e);
                throw new RuntimeException(e);
            }
        } else {
            ddsR = new DruidDataSource();
            ddsR.setUrl(getJdbcUrl());
            ddsR.setUsername(DB_USERNAME);
            ddsR.setPassword(getDbPwd(DB_USERNAME));
            ddsR.setMaxActive(20);
            ddsR.setMinIdle(3);
            ddsR.setInitialSize(3);
            ddsR.setMaxWait(10000);
            ddsR.setValidationQuery("SELECT 1");
            ddsR.setTimeBetweenEvictionRunsMillis(60000L);
            ddsR.setMinEvictableIdleTimeMillis(300000L);
            ddsR.setTestWhileIdle(true);
            ddsR.setTestOnBorrow(false);
            ddsR.setTestOnReturn(false);
            ddsR.setMaxPoolPreparedStatementPerConnectionSize(5);
            //建表语句适配FDB->改为动态设置该属性（如果是MySQL才设置）
            FileSystemResource resource = new FileSystemResource("sql/create_table_v0.1.sql");
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
            DatabasePopulatorUtils.execute(databasePopulator, ddsR);
            return ddsR;
        }
    }

    @SneakyThrows
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(ArrayUtils.addAll(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mysql/*Mapper.xml"),
                ArrayUtils.addAll(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/fdb/read/*Mapper.xml"),
                                  new PathMatchingResourcePatternResolver().getResources("classpath:mapper/comm/*Mapper.xml"))
            )
        );
        Properties props = new Properties();
        props.setProperty("callSettersOnNulls", "true");
//        props.setProperty("logImpl", "STDOUT_LOGGING");     //开发测试时用，生产必须还原
        factoryBean.setConfigurationProperties(props);
        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    private String getJdbcUrl(){
        String ipMaster = System.getenv("DB_HOST");
        //下面是测试配置，生产需要放开上面 TODO YK
//        String ipMaster = "172.16.71.74";
        String ipSlave = System.getenv("DB_HOST_SLAVE");
        String dbPort = System.getenv("DB_PORT");
        dbPort = dbPort == null ? DB_PORT : dbPort;
        String dbAddress;
        if (!StringUtils.isEmpty(ipMaster)) {
            dbAddress = ipMaster;
        } else {
            dbAddress = LOCALHOST;
        }
        if (!StringUtils.isEmpty(ipSlave)) {
            if (CommUtil.getDB_TYPE() == DBType.MYSQL) {
                dbAddress += COLON + dbPort + COMMA + ipSlave + COLON + dbPort + FORWARD_SLASH + DB_NAME;
            } else {
                dbAddress += COMMA + ipSlave + COLON + dbPort + COLON + DB_NAME;
            }
        } else {
            if (CommUtil.getDB_TYPE() == DBType.MYSQL) {
                dbAddress += COLON + dbPort + FORWARD_SLASH + DB_NAME;
            } else {
                dbAddress += COLON + dbPort + COLON + DB_NAME;
            }
        }
        if (CommUtil.getDB_TYPE() == DBType.MYSQL) {
            String oriUrl = "jdbc:mysql://" + dbAddress + "?useUnicode=true&failOverReadOnly=false&autoReconnect=true&allowMultiQueries=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";
            return DbSsl.getSslUrl(oriUrl);
        } else {
            return "jdbc:wrap-jdbc:filters=encoding:jdbc:oracle:thin:@" + dbAddress;
        }
    }

    private String getDbPwd(String dbUser) {
        try {
            return DBLogin.getPassword("os:" + dbUser);
        } catch (DBLoginException e) {
            log.error("GET_DBPWD_ERR", e);
            return DB_PWD;
        }
    }
}
