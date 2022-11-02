package com.envisioniot.uscada.monitor.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.ConnectorStartFailedException;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class MonitorWebApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(MonitorWebApplication.class, args);
        } catch (Exception e) {
            // 捕获端口占用-------
            String message = e.getMessage();
            if (e.getClass().isAssignableFrom(ConnectorStartFailedException.class) || (message.contains("listen on port") && message.contains("failed to start"))) {
                log.error("############App Exit#############");
                log.error(e.toString());
                log.error("port used, pls check.");
                System.exit(0);
            }
        }
    }
}