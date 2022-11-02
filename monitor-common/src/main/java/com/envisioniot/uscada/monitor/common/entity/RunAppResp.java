package com.envisioniot.uscada.monitor.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Data
@NoArgsConstructor
public class RunAppResp {
    private Long appPid;
    private String appUid;

    public RunAppResp(long pid, String name) {
        this.appPid = pid;
        this.appUid = name;
    }
}
