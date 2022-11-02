package com.envisioniot.uscada.monitor.transfer.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * CommConstants
 *
 * @author yangkang
 * @date 2021/8/30
 */
public interface CommConstants {
    /**常用日期格式**/
    String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern(TIME_FORMAT);
    String CACHE_TIMEFORMAT = "@dd-HH:mm";

    int ONEMINUTE_MILLIS = 60 * 1000;
}
