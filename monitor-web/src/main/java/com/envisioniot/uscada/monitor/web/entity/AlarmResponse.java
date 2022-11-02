package com.envisioniot.uscada.monitor.web.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * AlarmResponse
 *
 * @author yangkang
 * @date 2021/9/1
 */
@Getter
@Setter
public class AlarmResponse {
    private String occurTime;
    private String alarmObject;
    private String alarmContent;
    private short alarmType;
}
