package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;

/**
 * DockerSampleReq
 *
 * @author yangkang
 * @date 2021/1/22
 */
@Data
public class DockerSampleReq {
    private String st;
    private String et;
    private String hostIp;
    private String containerId;
}
