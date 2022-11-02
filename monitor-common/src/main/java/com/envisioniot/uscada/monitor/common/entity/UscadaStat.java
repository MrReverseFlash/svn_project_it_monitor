package com.envisioniot.uscada.monitor.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * UscadaStat
 *
 * @author yangkang
 * @date 2021/9/30
 */
@Data
@AllArgsConstructor
public class UscadaStat {
    private String ip;
    //1：scada正常运行；2：scada异常运行；3：scada未启动
    private short status;
}
