package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

import java.util.List;

/**
 * @author hao.luo
 * @date 2021-08-18
 */
@Data
public class DockerObj {
    private Boolean isDocker = Boolean.FALSE;
    private List<String> containerNames;
}
