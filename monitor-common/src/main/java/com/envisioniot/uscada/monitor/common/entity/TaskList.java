package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
@Data
public class TaskList implements Serializable {
    private static final long serialVersionUID = -5744095665704757914L;
    private TaskInfo host;
    private TaskInfo app;
    private TaskInfo db;
    private TaskInfo table;
    private TaskInfo docker;
    private TaskInfo http;
    private TaskInfo ied;
    private TaskInfo port;
    private TaskInfo topo;
}
