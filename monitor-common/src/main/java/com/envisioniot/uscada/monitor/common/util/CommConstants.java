package com.envisioniot.uscada.monitor.common.util;

/**
 * @author hao.luo
 * @date 2020-12-17
 */
public class CommConstants {

    /**
     * 状态正常
     */
    public static final int ONLINE_STATUS = 1;

    /**
     * 退出
     */
    public static final int OFFLINE_STATUS = 2;

    /**
     * 不监控
     */
    public static final int NO_MONITOR_STATUS = 3;

    /**
     * mysql 默认端口号
     */
    public static final String MYSQL_DEFAULT_PORT = "3306";

    /**
     * 默认监控数据库名称
     */
    public static final String DEFAULT_DB_NAME = "envision";

    /**
     * windows机器
     */
    public static final String WIN_FLAG = "win";

    /**
     * 主机资源保存post url
     */
    public static final String POST_HOST_SAMPLE_URL = "monitor-transfer/host/saveSample";

    /**
     * 获取监控app的信息
     */
    public static final String GET_APP_INFO_URL = "monitor-transfer/app/getInfo";

    /**
     * app监控信息保存url
     */
    public static final String POST_APP_SAMPLE_URL = "monitor-transfer/app/saveSample";


    /**
     * 获取需要监控docker容器信息
     */
    public static final String GET_DOCKER_INFO_URL = "monitor-transfer/docker/getInfo";

    /**
     * docker容器监控信息保存url
     */
    public static final String POST_DOCKER_SAMPLE_URL = "monitor-transfer/docker/saveSample";

    /**
     * 获取需要查询拓扑连接状态信息
     */
    public static final String GET_TOPO_INFO_URL = "monitor-transfer/topo/getInfo";

    /**
     * 拓扑连接状态信息保存url
     */
    public static final String POST_TOPO_SAMPLE_URL = "monitor-transfer/topo/saveSample";

    /**
     * 获取需要监控的http信息url
     */
    public static final String GET_HTTP_INFO_URL = "monitor-transfer/http/getInfo";
    /**
     * http 接口监控信息保存url
     */
    public static final String POST_HTTP_SAMPLE_URL = "monitor-transfer/http/saveSample";

    /**
     * 获取需要监控的ied信息url
     */
    public static final String GET_IED_INFO_URL = "monitor-transfer/ied/getInfo";

    /**
     * ied 监控信息保存url
     */
    public static final String POST_IED_SAMPLE_URL = "monitor-transfer/ied/saveSample";

    /**
     * 获取需要监控的端口信息url
     */
    public static final String GET_PORT_INFO_URL = "monitor-transfer/port/getInfo";
    /**
     * 端口监控信息保存url
     */
    public static final String POST_PORT_SAMPLE_URL = "monitor-transfer/port/saveSample";

    /**
     * 获取需要监控的数据库信息url
     */
    public static final String GET_DB_INFO_URL = "monitor-transfer/db/getInfo";
    /**
     * 数据库监控信息保存url
     */
    public static final String POST_DB_SAMPLE_URL = "monitor-transfer/db/saveSample";

    public static final String TOKEN_KEY = "envision";

    public static final String TOKEN_VAL = "#[SCADA]#";

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认时间格式
     */
    public static final String FILE_TIME_FORMAT = "yyyyMMddHHmmss";

    /**
     * 数据库监控信息保存url
     */
    public static final String POST_RUN_APP_URL = "monitor-agent/getRun?exp=%s&isDocker=%s&containerName=%s";

    public static final String HTTP = "http://";

    /**
     * 数据库连接测试
     */
    public static final String POST_DB_CONN_TEST_URL = "monitor-agent/testDbConn";

    public static final short ALARMTYPE_CPU = 1;
    public static final short ALARMTYPE_MEM = 2;
    public static final short ALARMTYPE_DISK = 3;
    public static final short ALARMTYPE_HOSTSTAT = 4;
    public static final short ALARMTYPE_DBSTAT = 5;
    public static final String DBOFFLINE = "数据库离线";
    public static final String HOSTOFFLINE = "主机离线";

    //数据库相关
    public static final String DB_USERNAME = "envision";
    public static final String DB_NAME = "envision";
    public static final String DB_PWD = "envision";
    public static final String DB_PORT = "3306";
    public static final String LOCALHOST = "127.0.0.1";
    public static final String COLON = ":";
    public static final String COMMA = ",";
    public static final String FORWARD_SLASH = "/";

    public static final String FDB_SRV_FILE = "/home/windos/envision/config/fdb_server.xml";
    public static final String FDB_APP_NAME = "fdb_config";
    public static final String FDB_SEC_NAME = "fdb_param";
    public static final String FDB_REC_NAME = "fdb_path";

    //SCADA告警推送接口相关
    //告警类型
    public static final String WARN_TYPE_ITMONITOR = "16";
}
