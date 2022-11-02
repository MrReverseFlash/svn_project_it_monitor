package com.envisioniot.uscada.monitor.common.util;

import com.envisioniot.uscada.monitor.common.enums.DBType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * CommUtil
 *
 * @author yangkang
 * @date 2021/5/25
 */
public final class CommUtil {
    //数据库相关

    @Getter
    private static final DBType DB_TYPE = dbTypeInit();

    private static DBType dbTypeInit(){
        String dbType = System.getenv("IS_MYSQL");
        if (dbType != null) {
            if (dbType.equals("1")) {
                return DBType.MYSQL;
            } else if (dbType.equals("3")) {
                return DBType.FDB;
            } else if (dbType.equals("2")) {
                return DBType.DMDB;
            }
        }
        return DBType.MYSQL;
    }
}
