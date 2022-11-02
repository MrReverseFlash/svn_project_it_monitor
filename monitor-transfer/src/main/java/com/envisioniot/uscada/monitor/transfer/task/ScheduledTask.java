package com.envisioniot.uscada.monitor.transfer.task;

import com.envisioniot.uscada.monitor.transfer.service.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * ScheduledTask
 *
 */
@ConditionalOnProperty(prefix = "uscada.monitor.transfer", name = "hostHealthCheck", havingValue = "true", matchIfMissing = true)
@Configuration
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Resource
    private AlarmService alarmService;

    @Async("taskExecutor")
    @Scheduled(initialDelay = 60000L, fixedDelay = 60000L)
    public void hostHealthCheck() {
        try {
            alarmService.hostHealthCheck();
        } catch (Exception e) {
            logger.error("check host health failed: ", e);
        }
    }
}
