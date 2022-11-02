package com.envisioniot.uscada.monitor.agent.service.monitor;

import com.envisioniot.uscada.monitor.common.entity.TaskInfo;


/**
 * @author hao.luo
 * @date 2020-12-22
 */
public interface TaskSchedule extends Runnable {
    /**
     * @return 获取计划任务的信息
     */
    public TaskInfo getTaskInfo();

    /**
     * @return 获取对应定时任务的uuid
     */
    public Integer getTaskUUID();

    /**
     * agent启动时候，保存默认需要监控的对象
     */
    public void initMonitor();
}
