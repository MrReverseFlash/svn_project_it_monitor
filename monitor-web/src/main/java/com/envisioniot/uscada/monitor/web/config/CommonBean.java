package com.envisioniot.uscada.monitor.web.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
@Configuration
public class CommonBean {

    @Bean
    public RestTemplate restTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        return new RestTemplateBuilder()
                .additionalMessageConverters(m)
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }
}
