package com.envisioniot.uscada.monitor.common.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hao.luo
 * @date 2020-12-25
 */
public class InfluxConstants {

    /**
     * host sample influx measures
     */
    public static final String INFLUX_HOST_HIS = "monitor_host_his";

    /**
     * host disk sample influx measures
     */
    public static final String INFLUX_DISK_HIS = "monitor_disk_his";

    /**
     * net card sample influx measures
     */
    public static final String INFLUX_NET_HIS = "monitor_net_his";

    /**
     * host app sample influx measures
     */
    public static final String INFLUX_APP_HIS = "monitor_app_his";

    /**
     * host docker container sample influx measures
     */
    public static final String INFLUX_DOCKER_HIS = "monitor_docker_his";

    /**
     * host port sample influx measures
     */
    public static final String INFLUX_PORT_HIS = "monitor_port_his";

    /**
     * host http sample influx measures
     */
    public static final String INFLUX_HTTP_HIS = "monitor_http_his";
    /**
     * host db sample influx measures
     */
    public static final String INFLUX_DB_HIS = "monitor_db_his";
    /**
     * host db sample influx measures
     */
    public static final String INFLUX_TABLE_HIS = "monitor_table_his";
    /**
     * host ied sample influx measures
     */
    public static final String INFLUX_IED_HIS = "monitor_ied_his";


    /*** influx tag ***/
    public static final String TAG_IP = "host_ip";
    public static final String TAG_DISK_NAME = "dir_name";
    public static final String TAG_NET_CARD_NAME = "net_card_name";
    public static final String TAG_APP_UID = "app_uid";
    public static final String TAG_CONTAINER_ID = "container_id";
    public static final String TAG_PORT = "port";
    public static final String TAG_HTTP_URL = "url";
    public static final String TAG_DB_IP = "db_ip";
    public static final String TAG_DB_TYPE = "db_type";
    public static final String TAG_DB_NAME = "db_name";
    public static final String TAG_TABLE_NAME = "table_name";
    public static final String TAG_PEER_IP = "peer_ip";

    /*** common field***/
    public static final String FIELD_THREAD_NUM = "thread_num";
    public static final String FIELD_CPU_PER = "cpu_per";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MEM_PER = "mem_per";
    public static final String FIELD_MEM_USE = "mem_use";
    public static final String FIELD_INODES_PER = "inodes_per";
    public static final String INFLUX_TIME_FIELD = "time";

    /***  host field ***/
    public static final String FIELD_LOGIN_USER_NUM = "login_user_num";
    public static final String FIELD_PROCESS_NUM = "process_num";
    public static final String FIELD_PROCESS_ZOMBIE = "process_zombie_num";
    public static final String FIELD_CPU_SYS = "cpu_sys";
    public static final String FIELD_CPU_USER = "cpu_user";
    public static final String FIELD_CPU_IDLE = "cpu_idle";
    public static final String FIELD_CPU_IOWAIT = "cpu_iowait";
    public static final String FIELD_CPU_IRQ = "cpu_irq";
    public static final String FIELD_CPU_SOFT = "cpu_soft";
    public static final String FIELD_MEM_TOTAL = "mem_total";
    public static final String FIELD_MEM_FREE = "mem_free";
    public static final String FIELD_ONE_LOAD = "one_load";
    public static final String FIELD_FIVE_LOAD = "five_load";
    public static final String FIELD_FIFTEEN_LOAD = "fifteen_load";
    public static final String FIELD_SWAP_TOTAL = "swap_total";
    public static final String FIELD_SWAP_USED = "swap_used";
    public static final String FIELD_SWAP_PER = "swap_per";
    public static final String FIELD_SWAP_PAGE_IN = "swap_page_in";
    public static final String FIELD_SWAP_PAGE_OUT = "swap_page_out";
    public static final String FIELD_NET_MAX_PER = "net_max_per";
    public static final String FIELD_NET_PER = "net_per";
    public static final String FIELD_DISK_PER = "disk_per";
    public static final String FIELD_DISK_USE = "disk_use";
    public static final String FIELD_DISK_TOTAL = "disk_total";

    /***  disk field ***/
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_USED = "used";
    public static final String FIELD_AVAIL = "avail";
    public static final String FIELD_USE_PER = "use_per";
    public static final String FIELD_READ_COUNT = "read_count";
    public static final String FIELD_WRITE_COUNT = "write_count";
    public static final String FIELD_READ_BYTES = "read_bytes";
    public static final String FIELD_WRITE_BYTES = "write_bytes";
    public static final String FIELD_READ_RATE = "read_rate";
    public static final String FIELD_WRITE_RATE = "write_rate";

    /***  net card field ***/
    public static final String FIELD_RXBYT = "rxbyt";
    public static final String FIELD_TXBYT = "txbyt";
    public static final String FIELD_RXPCK = "rxpck";
    public static final String FIELD_TXPCK = "txpck";
    public static final String FIELD_RXERRORS = "rxErrors";
    public static final String FIELD_TXERRORS = "txErrors";
    public static final String FIELD_RXDROPS = "rxDrops";
    public static final String FIELD_TXDROPS = "txDrops";
    public static final String FIELD_TOTAL_BANDWIDTH = "total_bandwidth";
    public static final String FIELD_TOTAL_NETFLOW = "total_netflow";
    public static final String FIELD_TOTAL_NET_IN = "total_net_in";
    public static final String FIELD_TOTAL_NET_OUT = "total_net_out";


    /***  app field ***/
    public static final String FIELD_IO_READ = "io_read";
    public static final String FIELD_IO_WRITE = "io_write";
    public static final String FIELD_APP_PID = "app_pid";
    public static final String FIELD_USER = "user";

    /*** docker field ***/
    public static final String FIELD_PIDS = "pids";
    public static final String FIELD_NET_IO_SEND = "net_io_send";
    public static final String FIELD_NET_IO_RECEIVE = "net_io_receive";
    public static final String FIELD_BLOCK_IO_WRITE = "block_io_write";
    public static final String FIELD_BLOCK_IO_READ = "block_io_read";

    /*** port field ***/
    public static final String FIELD_LISTENING_NUM = "listening_num";
    public static final String FIELD_ESTABLISHED_NUM = "established_num";
    public static final String FIELD_TIMEWAIT_NUM = "timewait_num";
    public static final String FIELD_CLOSEWAIT_NUM = "closewait_num";
    public static final String FIELD_SYNSENT_NUM = "synsent_num";
    public static final String FIELD_IDLE_NUM = "idle_num";

    /*** http field ***/
    public static final String FIELD_RESPONSE_TIME = "response_time";
    public static final String FIELD_RESPONSE_CODE = "response_code";

    /*** db field ***/
    public static final String FIELD_QUESTIONS = "questions";
    public static final String FIELD_COMMIT = "commit_num";
    public static final String FIELD_ROLLBACK = "rollback_num";
    public static final String FIELD_CONNMAX = "conn_max";
    public static final String FIELD_USECON = "use_conn";
    public static final String FIELD_TABLE_NUM = "record_num";

    public static String convertTag(String tag) {
        return "\"" + tag + "\"";
    }

    public static final Set<Character> RE_KEYS = new HashSet<>(Arrays.asList('"', '\\'));

    public static String convEspCharacter(CharSequence content) {
        StringBuilder builder = new StringBuilder();
        int len = content.length();

        for (int i = 0; i < len; ++i) {
            char current = content.charAt(i);
            if (RE_KEYS.contains(current)) {
                builder.append('\\');
            }
            builder.append(current);
        }
        return builder.toString();
    }
}

