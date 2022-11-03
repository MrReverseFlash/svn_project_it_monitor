package com.envisioniot.uscada.monitor.web.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.envisioniot.uscada.monitor.common.entity.RunAppResp;
import com.envisioniot.uscada.monitor.web.component.HttpComponent;
import com.envisioniot.uscada.monitor.web.dao.rdb.AppDao;
import com.envisioniot.uscada.monitor.web.dao.tsdb.AppInfluxDao;
import com.envisioniot.uscada.monitor.web.entity.*;
import com.envisioniot.uscada.monitor.web.exception.WebRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.HTTP;
import static com.envisioniot.uscada.monitor.common.util.CommConstants.POST_RUN_APP_URL;

/**
 * @author hao.luo
 * @date 2021-01-08
 */
@Service
@Slf4j
public class AppService {

    @Autowired
    private AppDao appDao;

    @Autowired
    private HttpComponent httpComponent;

    @Autowired
    private AppInfluxDao appInfluxDao;

    @Value("${host.matchFlag}")
    private String matchFlag;

    public List<AppTable> getAllMonitorApp() {
        List<AppTable> appTables = appDao.getAllApp();
        if (!CollectionUtils.isEmpty(appTables)) {
            appTables.forEach(appTable -> appTable.setHostMatch(matchFlag.equals(appTable.getMatchFlag())));
        }
        return appTables;
    }

    public List<AppTable> getAppByIp(String hostIp) {
        List<AppTable> appTables = appDao.getApp(hostIp);
        if (!CollectionUtils.isEmpty(appTables)) {
            appTables.forEach(appTable -> appTable.setHostMatch(matchFlag.equals(appTable.getMatchFlag())));
        }
        return appTables;
    }

    public void modifyName(Integer id, String name) {
        try {
            appDao.modifyName(id, name);
        } catch (Exception e) {
            log.error("modify name={}, app id={} fail.", name, id);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public void deleteApp(DeleteAppRequest request) {
        try {
            appInfluxDao.deleteAppHis(request.getAppUid(), request.getHostIp());
            appDao.deleteApp(request.getId());
        } catch (Exception e) {
            log.error("delete app={} fail.", request);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public List<AppSampleResp> getSample(AppSampleRequest request) {
        try {
            DateTime startTime = DateUtil.parse(request.getSt());
            DateTime endTime = DateUtil.parse(request.getEt());
            return appInfluxDao.getSample(startTime.getTime(), endTime.getTime(), request.getHostIp(), request.getAppUid());
        } catch (Exception e) {
            log.error("get app={} sample fail.", request);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public void addApp(AppAddRequest request) {
        try {
            appDao.addMonitorApp(request.getAppPid(), request.getAppUid(), request.getHostIp(), request.getName(), request.getContainerName());
        } catch (Exception e) {
            log.error("add monitor app={} fail.", request);
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }

    public List<RunAppResp> getRun(RunAppRequest request) {
        String containerName = request.getContainerName();
        if (containerName == null) {
            containerName = "";
        }
        String url = String.format(HTTP + request.getHostIp() + ":" + request.getPort() + "/" + POST_RUN_APP_URL, request.getExp(), request.getIsDocker() ? "true" : "false", containerName);
        try {
            Object result = httpComponent.getApi(url, 1);
            return JSONUtil.parse(result).toBean(new TypeReference<List<RunAppResp>>() {
            });
        } catch (Exception e) {
            log.error("get run app fail, url={}", url);
            log.error(e.getMessage(), e);
            throw new WebRequestException("get run app fail.");
        }
    }
}
