package com.envisioniot.uscada.monitor.agent.service.monitor;

import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.agent.component.HttpComponent;
import com.envisioniot.uscada.monitor.agent.config.CommonConfig;
import com.envisioniot.uscada.monitor.agent.config.TaskConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author hao.luo
 * @date 2020-12-23
 */
@Slf4j
public abstract class AbstractMonitorService<T, U> implements TaskSchedule {

    @Autowired
    protected CommonConfig commonConfig;

    @Autowired
    protected HttpComponent httpComponent;

    @Autowired
    protected TaskConfig taskConfig;

    /**
     * @return 获取监控对象的采样数据
     */
    public abstract T getMonitorSample(U request);

    /**
     * @return 资源采样的保存server端的url
     */
    public abstract String samplePostUrl();

    /**
     * @return 获取监控对象的server端url
     */
    public abstract String getMonitorObjUrl();

    /**
     * @param obj 获取监控对象
     * @return 转变对对应的类型的class object
     */
    public abstract U convertObject(Object obj);

    /**
     * @return 获取监控的对象
     */
    public U getMonitorObj() {
        U monitorObj = null;
        if (commonConfig.isHttp()) {
            Integer retryNum = commonConfig.getRetryNum();
            // 获取需要监控的app
            String url = getMonitorObjUrl();
            if (!StringUtils.isEmpty(url)) {
                Object data = httpComponent.getApi(url, retryNum);
                if (data != null) {
                    monitorObj = convertObject(data);
                }
            }
        }
        return monitorObj;
    }

    /**
     * @param sample ：向server端保存监控采样结果
     */
    public void pushSampleResult(T sample) {
        if (commonConfig.isHttp()) {
            String url = samplePostUrl();
            httpComponent.postForServer(url, JSONUtil.parseObj(sample), commonConfig.getRetryNum());
        } else {
            log.error("agent push sample to server by http connection,pls check.");
        }
    }

    public void pushInitMonitorObj(T sample) {
        if (commonConfig.isHttp()) {
            String url = samplePostUrl();
            if (url.contains("?")) {
                if (url.endsWith("?") || url.endsWith("&")) {
                    url += "isInit=true";
                } else {
                    url += "&isInit=true";
                }
            } else {
                url += "?isInit=true";
            }
            httpComponent.postForServer(url, JSONUtil.parseObj(sample), commonConfig.getRetryNum());
        } else {
            log.error("agent push sample to server by http connection,pls check.");
        }
    }

    @Override
    public void run() {
        String name = getTaskInfo().getName();
        log.info("----开始{}资源监控任务----", name);
        try {
            U monitorObj = getMonitorObj();
            T monitorSample = getMonitorSample(monitorObj);
            if (monitorSample != null) {
                pushSampleResult(monitorSample);
            }
        } catch (Exception e) {
            log.error("执行{}资源监控任务失败!", name);
            log.error(e.getMessage(), e);
        }
        log.info("----完成{}资源监控任务----", name);
    }

    @Override
    public void initMonitor() {
        log.info("no default object to monitor.");
        // ignore
    }
}
