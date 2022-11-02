package com.envisioniot.uscada.monitor.web.util;

import com.envisioniot.uscada.monitor.common.util.CommConstants;

import java.time.format.DateTimeFormatter;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
public class Constants {
    public static final DateTimeFormatter SECOND_DF = DateTimeFormatter.ofPattern(CommConstants.DEFAULT_TIME_FORMAT);
}
