package com.envisioniot.uscada.monitor.common.entity;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
public enum TaskEnum {
    HOST_TASK(100),
    APP_TASK(101),
    DB_TASK( 102),
    DB_TABLE_TASK( 103),
    DOCKER_TASK(104),
    HTTP_TASK(105),
    IED_TASK(106),
    PORT_TASK(107),
    TOPO_TASK(108);

    public Integer getUuid() {
        return uuid;
    }

    private final Integer uuid;

    TaskEnum(Integer uuid) {
        this.uuid = uuid;
    }


}
