package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class HttpStat extends HttpInfo implements Serializable {
    private static final long serialVersionUID = 1715868258624560117L;
    private Integer status;
    private Integer code;
    private Long responseTime;
    private String occurTime;
    private String msg;
}
