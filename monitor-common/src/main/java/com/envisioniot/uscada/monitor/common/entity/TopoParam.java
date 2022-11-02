package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;

/**
 * TopoParam
 *
 * @author yangkang
 * @date 2021/3/2
 */
@Data
public class TopoParam {

    /**
     * 改用UUID
     */
    private String id;

    /**
     * 序号
     */
    private Short num;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型：1:level; 2:set; 3:physic
     */
    private Short type;

}
