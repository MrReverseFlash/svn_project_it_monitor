package com.envisioniot.uscada.monitor.transfer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.ConnectorStartFailedException;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author hao.luo
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class MonitorTransferApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(MonitorTransferApplication.class, args);
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
