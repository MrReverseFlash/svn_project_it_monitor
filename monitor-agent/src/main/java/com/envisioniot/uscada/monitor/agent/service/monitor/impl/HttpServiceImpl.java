package com.envisioniot.uscada.monitor.agent.service.monitor.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.agent.service.monitor.AbstractMonitorService;
import com.envisioniot.uscada.monitor.common.entity.*;
import com.envisioniot.uscada.monitor.common.util.CommConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;

/**
 * @author hao.luo
 * @date 2020-12-22
 */
@Service
@Slf4j
public class HttpServiceImpl extends AbstractMonitorService<CommStat<HttpStat>, List<HttpInfo>> {

    @Override
    public CommStat<HttpStat> getMonitorSample(List<HttpInfo> httpInfoList) {
        if (CollectionUtils.isEmpty(httpInfoList)) {
            log.warn("monitor obj is empty.");
            return null;
        }
        CommStat<HttpStat> response = new CommStat<>();
        response.setHostIp(commonConfig.getLocalIp());
        List<HttpStat> httpStats = new ArrayList<>();
        httpInfoList.forEach((httpInfo -> {
            HttpStat httpStat = monitor(httpInfo);
            httpStats.add(httpStat);
        }));
        response.setData(httpStats);
        return response;
    }

    private HttpStat monitor(HttpInfo httpInfo) {
        HttpStat httpStat = new HttpStat();
        BeanUtils.copyProperties(httpInfo, httpStat);
        httpStat.setOccurTime(DateUtil.now());
        try {
            String url = httpInfo.getUrl();
            HttpMethod method = HttpMethod.getMethod(httpInfo.getMethod());
            long beginTime = System.currentTimeMillis();
            int code;
            switch (method) {
                case GET:
                    code = httpComponent.get(url);
                    httpStat.setCode(code);
                    break;
                case POST:
                    code = httpComponent.post(url, httpInfo.getBody());
                    httpStat.setCode(code);
                    break;
                default:
                    throw new IllegalArgumentException("http method = " + method + " not support.");
            }
            long responseTime = System.currentTimeMillis() - beginTime;
            httpStat.setResponseTime(responseTime);
            if (validResponseCode(code)) {
                httpStat.setStatus(CommConstants.ONLINE_STATUS);
            } else {
                httpStat.setStatus(CommConstants.OFFLINE_STATUS);
                HttpStatus httpStatus = HttpStatus.valueOf(code);
                httpStat.setMsg(httpStatus.getReasonPhrase());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            httpStat.setMsg(e.getMessage());
            httpStat.setStatus(CommConstants.OFFLINE_STATUS);
        }
        return httpStat;
    }

    private boolean validResponseCode(Integer code) {
        return code != null && code < HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskConfig.getTaskList().getHttp();
    }

    @Override
    public Integer getTaskUUID() {
        return TaskEnum.HTTP_TASK.getUuid();
    }

    @Override
    public String samplePostUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.POST_HTTP_SAMPLE_URL;
    }

    @Override
    public String getMonitorObjUrl() {
        return HTTP + commonConfig.getServerIp() + ":" + commonConfig.getServerPort() + "/" + CommConstants.GET_HTTP_INFO_URL + "?hostIp=" + commonConfig.getLocalIp();
    }

    @Override
    public List<HttpInfo> convertObject(Object obj) {
        return JSONUtil.parse(obj).toBean(new TypeReference<List<HttpInfo>>() {
        });
    }
}
