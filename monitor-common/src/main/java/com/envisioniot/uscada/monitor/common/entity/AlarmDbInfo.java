package com.envisioniot.uscada.monitor.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * AlarmDbInfo
 *
 * @author yangkang
 * @date 2021/9/1
 */
@Getter
@Setter
public class AlarmDbInfo {
    private String dbId;
    private String occurTime;
    private short alarmType;
    private String alarmContent;
}
