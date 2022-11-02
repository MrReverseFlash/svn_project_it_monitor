package com.envisioniot.uscada.monitor.transfer.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * CacheConfig
 * 缓存配置
 *
 * @author yangkang
 * @date 2021/8/13
 */
@Configuration
@EnableConfigurationProperties(AlarmProperties.class)
@EnableAsync
public class CacheConfig {

    @Autowired
    private AlarmProperties alarmProperties;

    @Bean
    @ConditionalOnProperty(prefix = "alarm.cpu", name = {"sampleDuration", "alarmThreshold"})
    public Cache<String, Object> cpuCache() {
        AlarmProp alarmProp = alarmProperties.getCpu();
        Short sampleDuration = alarmProp.getSampleDuration();
        return Caffeine.newBuilder()
                // 设置最后一次访问后经过多久过期
                .expireAfterAccess(sampleDuration, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(20)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "alarm.mem", name = {"sampleDuration", "alarmThreshold"})
    public Cache<String, Object> memCache() {
        AlarmProp alarmProp = alarmProperties.getMem();
        Short sampleDuration = alarmProp.getSampleDuration();
        return Caffeine.newBuilder()
                // 设置最后一次访问后经过多久过期
                .expireAfterAccess(sampleDuration, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(20)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "alarm.disk", name = {"sampleDuration", "alarmThreshold"})
    public Cache<String, Object> diskCache() {
        AlarmProp alarmProp = alarmProperties.getDisk();
        Short sampleDuration = alarmProp.getSampleDuration();
        return Caffeine.newBuilder()
                // 设置最后一次访问后经过多久过期
                .expireAfterAccess(sampleDuration, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(20)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "alarm.host", name = {"sampleDuration", "alarmThreshold"})
    public Cache<String, Object> hostCache() {
        AlarmProp alarmProp = alarmProperties.getHost();
        Short sampleDuration = alarmProp.getSampleDuration();
        return Caffeine.newBuilder()
                // 设置最后一次访问后经过多久过期
                .expireAfterAccess(sampleDuration, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(20)
                .build();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(2);
        executor.setQueueCapacity(8);
        executor.setMaxPoolSize(9);
        executor.setThreadNamePrefix("Alarm-handler-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        return executor;
    }
}
