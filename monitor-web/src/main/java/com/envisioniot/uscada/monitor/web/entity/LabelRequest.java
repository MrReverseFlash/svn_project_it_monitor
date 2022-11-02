package com.envisioniot.uscada.monitor.web.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author hao.luo
 * @date 2021-01-07
 */
@Data
@ToString
public class LabelRequest {
    private Integer id;
    private String label;
}
