package com.envisioniot.uscada.monitor.agent.util;

import java.util.regex.Pattern;

/**
 * @author hao.luo
 * @date 2020-12-16
 */
public class Constants {
    public static final String DEFAULT_TRANSPORTER = "http";

    public static final String DEFAULT_LOCAL_IP = "127.0.0.1";

    public static final String ANYHOST = "0.0.0.0";

    public static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    public static final float MB = 1024f * 1024f;

    public static final int DEFAULT_PRECISION = 1;

    public static final float PERCENT = 100.0f;

    public static final int PERCENT_PRECISION = 2;

    public static final Long ALL_PORT_FLAG = 243L;

    public static final Long TCP_PORT_FLAG = 19L;

    public static final Long UDP_PORT_FLAG = 35L;

    public static final Integer UDP_TYPE = 32;

    public static final Integer TCP_TYPE = 16;

    public static final String CONTAINER_ID = "id";

    public static final String CONTAINER_CREATE_TIME = "createTime";

    public static final String CONTAINER_UP_TIME = "upTime";

    public static final String CONTAINER_NAME = "name";

    public static final String CONTAINER_STATUS = "status";

    public static final String CONTAINER_IMAGE = "image";

    public static final String CONTAINER_MEMORY_PER = "memPer";

    public static final String CONTAINER_CPU_PER = "cpuPer";

    public static final String CONTAINER_PIDS = "pids";

    public static final String CONTAINER_NET_IO = "netIO";

    public static final String CONTAINER_BLOCK_IO = "blockIO";

    /**
     * search all container
     */
    public static final String[] DOCKER_PS = {"docker", "ps", "--format", "{\"id\":\"{{ .ID }}\",\"createTime\":\"{{ .CreatedAt }}\",\"name\":\"{{ .Names }}\",\"status\":\"{{ .Status}}\",\"image\":\"{{ .Image}}\"}"};

    /**
     * get all container status
     */
    public static final String[] DOCKER_STATS = {"docker", "stats", "--no-stream", "--format", "{\"id\":\"{{ .Name }}\",\"memPer\":\"{{ .MemPerc }}\",\"cpuPer\":\"{{ .CPUPerc }}\",\"pids\":\"{{ .PIDs }}\",\"netIO\":\"{{ .NetIO }}\",\"blockIO\":\"{{ .BlockIO }}\"}"};
//    public static final String DOCKER_STATS = String.format("docker stats --no-stream --format \"{\\\"%s\\\":\\\"{{ .ID }}\\\",\\\"%s\\\":\\\"{{ .MemPerc }}\\\",\\\"%s\\\":\\\"{{ .CPUPerc }}\\\",\\\"%s\\\":\\\"{{ .PIDs }}\\\",\\\"%s\\\":\\\"{{ .NetIO }}\\\",\\\"%s\\\":\\\"{{ .BlockIO }}\\\"}\"",
//            CONTAINER_ID, CONTAINER_MEMORY_PER, CONTAINER_CPU_PER, CONTAINER_PIDS, CONTAINER_NET_IO, CONTAINER_BLOCK_IO
//    );

    public static final String APP_DELIMIT = "!@!";

    /**
     * 获取所有正在运行容器的pid和容器名称
     */
    public static final String[] DOCKER_PID = {"docker", "inspect", "--format", "{\"pid\":\"{{ .State.Pid }}\",\"name\":\"{{ .Name }}\"}"};

    /**
     * 获取所有运行的容器id
     */
    public static final String[] DOCKER_CONTAINER_ID = {"docker", "ps", "-q"};

    /**
     * 获取容器下面进程的PID标签
     */
    public static final String DOCKER_TOP_PID_TAG = "PID";
}
