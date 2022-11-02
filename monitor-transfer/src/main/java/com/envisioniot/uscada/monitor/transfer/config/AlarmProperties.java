package com.envisioniot.uscada.monitor.transfer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * AlarmProperties
 * 平均采样告警配置（像主机状态、端口状态等告警写死，不可配）
 *
 * @author yangkang
 * @date 2021/8/13
 */
@ConfigurationProperties(prefix = "alarm")
@Getter
@Setter
@Validated
public class AlarmProperties {

    /**
     * CPU告警（如果连续X分钟均值大于Y则告警，下同）
     */
    @Valid
    private AlarmProp cpu;

    /**
     * 内存告警
     */
    @Valid
    private AlarmProp mem;

    /**
     * DISK分区告警
     */
    @Valid
    private AlarmProp disk;

    /**
     * host离线告警
     */
    @Valid
    private AlarmProp host;
}
