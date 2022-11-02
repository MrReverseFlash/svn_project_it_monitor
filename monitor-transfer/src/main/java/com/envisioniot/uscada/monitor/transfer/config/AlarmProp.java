package com.envisioniot.uscada.monitor.transfer.config;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * AlarmProp
 * 告警属性配置
 *
 * @author yangkang
 * @date 2021/8/13
 */
@Getter
@Setter
public class AlarmProp {
    /**
     * 采样时间（单位：分钟，最大值：1440，即1天）
     */
    @NotNull(message = "sampleDuration must not be null")
    @Min(1)
    @Max(1440)
    private Short sampleDuration;

    /**
     * 告警阈值（单位：%，采样平均大于该值，才可能告警）
     */
    @Min(0)
    @Max(100)
    @NotNull(message = "alarmThreshold must not be null")
    private Float alarmThreshold;

    /**
     * 告警间隔：上次告警中断后，多久后可以触发新的告警（单位：分钟，最大值：1440，默认为2，即上一次告警隔了2分钟才能产生新告警，最好比采样间隔大1）
     */
    @Min(1)
    @Max(1440)
    private short alarmInterval = 2;

    /**
     * 告警内容（不能为空，最多1024个字符）
     */
    @Size(min = 1, max = 1024)
    @NotNull(message = "alarmContent must not be null")
    private String alarmContent;

    /**
     * 符合要求监控的主机（不能为空，最多1024个字符）
     */
    @Size(min = 1, max = 1024)
    private String matchFlag;
}
