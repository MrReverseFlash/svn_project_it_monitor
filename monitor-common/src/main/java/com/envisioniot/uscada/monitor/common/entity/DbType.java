package com.envisioniot.uscada.monitor.common.entity;

/**
 * @author hao.luo
 * @date 2020-12-21
 */
public enum DbType {
    MYSQL("mysql"),
    POSTGRESQL("postgresql"),
    SQLSERVER("sqlserver"),
    DB2("db2"),
    ORACLE("oracle");

    public String getDbType() {
        return dbType;
    }

    private final String dbType;

    DbType(String dbType) {
        this.dbType = dbType;
    }

    public static DbType getDB(String dbType) {
        for (DbType value : DbType.values()) {
            if (value.dbType.equalsIgnoreCase(dbType)) {
                return value;
            }
        }
        throw new IllegalArgumentException("db type = " + dbType + " not support.");
    }
}
