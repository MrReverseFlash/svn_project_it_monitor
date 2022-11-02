package com.envisioniot.uscada.monitor.transfer.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.concurrent.TimeUnit;

/**
 * @author hao.luo
 */
@Configuration
@ConfigurationProperties(prefix = "spring.influx")
@Validated
@Slf4j
@Data
public class InfluxdbConfig implements DisposableBean {

    @NotEmpty
    private String url;

    @NotEmpty
    private String user;

    @NotEmpty
    private String password;

    @NotEmpty
    private String database;

    private String retentionPolicy;

    private int connectTimeout = 15;

    private int readTimeout = 90;

    private int writeTimeout = 10;

    private boolean gzip = true;

    private boolean enableBatch = true;

    private int batchSize = 1000;

    private int flushDuration = 100;

    private int logLevel = 0;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(value = "influxDB")
    @Primary
    public InfluxDB createInfluxdb() {
        final OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS);
        InfluxDB influxdb = InfluxDBFactory.connect(url, user, password, client);
        if (log.isDebugEnabled()) {
            log.debug("Using InfluxDB '{}' on '{}'", database, url);
        }
        if (gzip) {
            log.debug("Enabled gzip compression for HTTP requests");
            influxdb.enableGzip();
        }
        if (enableBatch) {
            BatchOptions options = BatchOptions.DEFAULTS
                    .threadFactory(taskExecutor)
                    .actions(batchSize)
                    .flushDuration(flushDuration);
            // T每次定期任务启动时候开启batch
            influxdb.enableBatch(options);
        }
        influxdb.setDatabase(database);
        setInfluxLogLevel(influxdb, logLevel);
        return influxdb;
    }

    private void setInfluxLogLevel(InfluxDB influxdb, Integer logLevel) {
        switch (logLevel) {
            case 1:
                influxdb.setLogLevel(InfluxDB.LogLevel.BASIC);
                break;
            case 2:
                influxdb.setLogLevel(InfluxDB.LogLevel.HEADERS);
                break;
            case 3:
                influxdb.setLogLevel(InfluxDB.LogLevel.FULL);
                break;
            default:
                influxdb.setLogLevel(InfluxDB.LogLevel.NONE);
        }
    }

    @Override
    public void destroy() {
        InfluxDB influxdb = applicationContext.getBean(InfluxDB.class);
        if (enableBatch) {
            influxdb.disableBatch();
        }
        influxdb.close();
    }
}
