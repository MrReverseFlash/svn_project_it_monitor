package com.envisioniot.uscada.monitor.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * AlarmHostInfo
 *
 * @author yangkang
 * @date 2021/9/1
 */
@Getter
@Setter
public class AlarmHostInfo {
    private String hostIp;
    private String occurTime;
    private short alarmType;
    private String alarmContent;
}
