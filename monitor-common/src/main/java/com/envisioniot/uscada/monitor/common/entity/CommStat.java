package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-16
 */
@Data
public class CommStat<T> implements Serializable {

    private static final long serialVersionUID = 41276711572642819L;

    private String hostIp;

    private List<T> data;

}
