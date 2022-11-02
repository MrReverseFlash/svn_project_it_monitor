package com.envisioniot.uscada.monitor.agent.schedule;

import com.envisioniot.uscada.monitor.agent.config.TaskConfig;
import com.envisioniot.uscada.monitor.agent.exception.TaskException;
import com.envisioniot.uscada.monitor.agent.service.monitor.TaskSchedule;
import com.envisioniot.uscada.monitor.common.entity.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
@Service
@Slf4j
public class ScheduledTaskService implements InitializingBean {

    @Autowired
    private List<TaskSchedule> taskScheduleList;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private TaskConfig taskConfig;

    /**
     * 启动的task，key为task uuid
     */
    private final Map<Integer, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>(8);

    private final ReentrantLock lock = new ReentrantLock();

    private TaskSchedule getTaskSchedule(int uuid) {
        for (TaskSchedule task : taskScheduleList) {
            if (uuid == task.getTaskUUID()) {
                return task;
            }
        }
        throw new TaskException("task not support.");
    }

    /**
     * @return 获取所有的定时任务， 运行和停止的
     */
    public List<TaskInfo> getAllTask() {
        List<TaskInfo> all = new ArrayList<>(8);
        taskScheduleList.forEach((task) -> {
            TaskInfo taskInfo = task.getTaskInfo();
            Integer uuid = task.getTaskUUID();
            taskInfo.setUuid(uuid);
            all.add(taskInfo);
        });
        return all;
    }

    public void startTask(int uuid) {
        TaskSchedule schedule = getTaskSchedule(uuid);
        lock.lock();
        try {
            doStart(schedule);
            taskConfig.saveYml();
        } finally {
            lock.unlock();
        }
    }

    public void stopTask(int uuid) {
        TaskSchedule schedule = getTaskSchedule(uuid);
        lock.lock();
        try {
            doStop(schedule);
            taskConfig.saveYml();
        } finally {
            lock.unlock();
        }
    }

    public void restartTask(int uuid) {
        TaskSchedule schedule = getTaskSchedule(uuid);
        lock.lock();
        try {
            doRestart(schedule);
            taskConfig.saveYml();
        } finally {
            lock.unlock();
        }
    }

    public void modifyTask(int uuid, String cron) {
        TaskSchedule schedule = getTaskSchedule(uuid);
        setCron(schedule, cron);
    }

    private void setCron(TaskSchedule schedule, String cron) {
        lock.lock();
        try {
            // modify cron
            TaskInfo taskInfo = schedule.getTaskInfo();
            taskInfo.setCron(cron);
            // 重启任务
            doRestart(schedule);
            taskConfig.saveYml();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 根据任务key 启动任务
     */
    private void doStart(TaskSchedule scheduleTask) {
        TaskInfo task = scheduleTask.getTaskInfo();
        Integer uuid = scheduleTask.getTaskUUID();
        if (this.isStart(uuid)) {
            throw new TaskException(String.format("定时任务={%s}已经被启动过了", task));
        }
        this.doStartTask(scheduleTask);
        log.info(">>>>>> 启动任务 {} 结束 >>>>>>", task);
    }

    /**
     * 停止任务
     */
    private void doStop(TaskSchedule scheduledTask) {
        log.info(">>>>>> 开始停止任务 {}  >>>>>>", scheduledTask.getTaskInfo());
        //当前任务实例是否存在
        Integer uuid = scheduledTask.getTaskUUID();
        boolean taskStartFlag = scheduledFutureMap.containsKey(uuid);
        if (taskStartFlag) {
            //获取任务实例
            ScheduledFuture scheduledFuture = scheduledFutureMap.get(uuid);
            //关闭实例
            scheduledFuture.cancel(true);
            scheduledFutureMap.remove(uuid);
        }
        scheduledTask.getTaskInfo().setRun(false);
        log.info(">>>>>> 结束停止任务 {}  >>>>>>", scheduledTask.getTaskInfo());
    }

    /**
     * 根据任务key 重启任务
     */
    public void doRestart(TaskSchedule scheduledTask) {
        TaskInfo taskInfo = scheduledTask.getTaskInfo();
        log.info(">>>>>> 进入重启任务 {}  >>>>>>", taskInfo);
        this.doStop(scheduledTask);
        this.doStart(scheduledTask);
        log.info(">>>>>> 进入重启任务 {} 结束 >>>>>>", taskInfo);
    }

    /**
     * 执行启动任务
     */
    private void doStartTask(TaskSchedule scheduledTask) {
        Integer uuid = scheduledTask.getTaskUUID();
        TaskInfo taskInfo = scheduledTask.getTaskInfo();
        String taskCron = taskInfo.getCron();
        //获取需要定时调度的接口
        log.info(">>>>>> 任务 [ {} ] 启动", taskInfo);
        ScheduledFuture scheduledFuture = threadPoolTaskScheduler.schedule(scheduledTask,
                triggerContext -> {
                    CronTrigger cronTrigger = new CronTrigger(taskCron);
                    return cronTrigger.nextExecutionTime(triggerContext);
                });
        scheduledFutureMap.put(uuid, scheduledFuture);
        scheduledTask.getTaskInfo().setRun(true);
    }

    /**
     * 任务是否已经启动
     */
    private Boolean isStart(Integer uuid) {
        if (scheduledFutureMap.containsKey(uuid)) {
            return !scheduledFutureMap.get(uuid).isCancelled();
        }
        return false;
    }

    /**
     * 程序启动时初始化  ==> 启动所有正常状态的任务
     */
    private void initAllTask() {
        if (CollectionUtils.isEmpty(taskScheduleList)) {
            return;
        }
        lock.lock();
        try {
            for (TaskSchedule scheduledTask : taskScheduleList) {
                TaskInfo taskInfo = scheduledTask.getTaskInfo();
                if (!taskInfo.isRun()) {
                    continue;
                }
                this.doStart(scheduledTask);
            }
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void afterPropertiesSet() {
        // 启动时候校验需要监控的默认对象
        taskScheduleList.forEach(taskSchedule -> {
            TaskInfo taskInfo = taskSchedule.getTaskInfo();
            if (!taskInfo.isRun()) {
                return;
            }
            log.info("init monitor task={}", taskInfo);
            taskSchedule.initMonitor();
            log.info("finish monitor task={}", taskInfo);
        });
        // 启动所有的定时任务
        initAllTask();
    }
}
