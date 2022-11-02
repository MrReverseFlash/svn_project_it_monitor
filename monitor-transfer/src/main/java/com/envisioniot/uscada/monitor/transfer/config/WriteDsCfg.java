package com.envisioniot.uscada.monitor.transfer.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Properties;

/**
 * WriteDsCfg
 *
 * @author yangkang
 * @date 2021/5/26
 */
@Slf4j
@Configuration
@Conditional(IsFdbCondition.class)
@MapperScan(basePackages = {"com.envisioniot.uscada.monitor.transfer.mapper.fdb.write"},
            sqlSessionFactoryRef = "sqlSessionFactoryW")
public class WriteDsCfg {

    private DruidDataSource ddsW;

    @PreDestroy
    public void destroy(){
        if (ddsW != null && !ddsW.isClosed()) {
            ddsW.setRemoveAbandonedTimeout(0);
            ddsW.removeAbandoned();
            ddsW.close();
        }
    }

    @Bean(name = "dataSourceW")
    public DruidDataSource dataSourceW(){
        String fdbUrl;
        String fdbDriverClassName;
        ArrayList<String> initSqlsList = new ArrayList<>(1);
        String dbHostEnv = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        dbPort = dbPort == null ? "6535" : dbPort;
        if (StringUtils.isEmpty(dbHostEnv)) {
            dbHostEnv = "127.0.0.1";
        }
        fdbUrl = "jdbc:sqlite-remote://" + dbHostEnv + ":" + dbPort + "/envision?user=envision&password=envision";
        fdbDriverClassName = "com.envision.uscada.fdb.remote.FdbRemoteDriver";
        try {
            //update、insert、delete从写线程池（单连接）获取
            ddsW = new DruidDataSource();
            ddsW.setUrl(fdbUrl);
            ddsW.setDriverClassName(fdbDriverClassName);
            ddsW.setDbType("FDB");
            ddsW.setLoginTimeout(5);
            ddsW.setQueryTimeout(60);
            ddsW.setPhyTimeoutMillis(65000L);
            ddsW.setKillWhenSocketReadTimeout(true);
            ddsW.setInitialSize(1);
            ddsW.setMaxActive(1);
            ddsW.setMinIdle(1);
            ddsW.setValidationQuery("select count(1) num from fac_info");
            ddsW.setValidationQueryTimeout(5);
            ddsW.setTestWhileIdle(true);
            ddsW.setTestOnBorrow(false);
            ddsW.setTestOnReturn(false);
            ddsW.setTimeBetweenEvictionRunsMillis(30000L);
            ddsW.setMaxWait(70000L);
            ddsW.setPoolPreparedStatements(false);
            ddsW.setMaxPoolPreparedStatementPerConnectionSize(-1);
            ddsW.setConnectionErrorRetryAttempts(3);
            ddsW.setNotFullTimeoutRetryCount(3);
            ddsW.setBreakAfterAcquireFailure(false);
            ddsW.setLogAbandoned(true);
            ddsW.setRemoveAbandoned(true);
            ddsW.setRemoveAbandonedTimeout(65);
            ddsW.setAsyncInit(true);
            ddsW.setConnectionInitSqls(initSqlsList);
            ddsW.init();
            return ddsW;
        } catch (Exception e) {
            log.error("GET_FDBCONN_ERR", e);
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Bean(name = "sqlSessionFactoryW")
    public SqlSessionFactory sqlSessionFactoryW(@Qualifier("dataSourceW") DataSource dataSource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/fdb/write/*Mapper.xml"));
        Properties props = new Properties();
        props.setProperty("callSettersOnNulls", "true");
//        props.setProperty("logImpl", "STDOUT_LOGGING");     //开发测试时用，生产必须还原
        factoryBean.setConfigurationProperties(props);
        return factoryBean.getObject();
    }

    @Bean(name = "transactionManagerW")
    public DataSourceTransactionManager transactionManagerW(@Qualifier("dataSourceW") DataSource dataSource){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
