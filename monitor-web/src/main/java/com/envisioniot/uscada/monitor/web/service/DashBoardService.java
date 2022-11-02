package com.envisioniot.uscada.monitor.web.service;

import com.envisioniot.uscada.monitor.web.dao.rdb.DashBoardDao;
import com.envisioniot.uscada.monitor.web.entity.AlarmResponse;
import com.envisioniot.uscada.monitor.web.entity.DashBoard;
import com.envisioniot.uscada.monitor.web.exception.WebRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * DashBoardService
 *
 * @author yangkang
 * @date 2021/3/3
 */
@Service
@Slf4j
public class DashBoardService {

    @Autowired
    private DashBoardDao dashBoardDao;

    public DashBoard init(){
        DashBoard dashBoard = new DashBoard();
        dashBoard.setHostNum(dashBoardDao.qryHostNum());
        dashBoard.setDbNum(dashBoardDao.qryDbNum());
        dashBoard.setAppNum(dashBoardDao.qryAppNum());
        dashBoard.setPortNum(dashBoardDao.qryPortNum());
        dashBoard.setHostOnLineNum(dashBoardDao.qryOnLineNum());
        dashBoard.setHostOffLineNum(dashBoardDao.qryOffLineNum());
        dashBoard.setCpuTmpTop10(dashBoardDao.qryCpuTmpTop10());
        dashBoard.setCpuPerTop10(dashBoardDao.qryCpuPerTop10());
        dashBoard.setMemPerTop10(dashBoardDao.qryMemPerTop10());
        dashBoard.setDiskPerTop10(dashBoardDao.qryDiskPerTop10());
        dashBoard.setNetPerTop10(dashBoardDao.qryNetPerTop10());
        //统计scada状态
        int uscadaRunningNum = 0;
        int uscadaExitNum = 0;
        List<String> scadaStatList = dashBoardDao.qryAllScadaStat();
        if (!CollectionUtils.isEmpty(scadaStatList)) {
            for (String scadaStatInfos : scadaStatList) {
                if (StringUtils.isNotBlank(scadaStatInfos)) {
                    String[] scadaStatInfoArr = scadaStatInfos.split(";");
                    for (String scadaStatInfo : scadaStatInfoArr) {
                        if (StringUtils.isNotBlank(scadaStatInfo)) {
                            String scadaStat = scadaStatInfo.split(":")[1];
                            if ("1".equals(scadaStat)) {
                                uscadaRunningNum++;
                            } else  {
                                uscadaExitNum++;
                            }
                        }
                    }
                }
            }
        }
        dashBoard.setUscadaRunningNum(uscadaRunningNum);
        dashBoard.setUscadaExitNum(uscadaExitNum);
        return dashBoard;
    }

    public List<AlarmResponse> getAllAlarm() {
        try {
            return dashBoardDao.getAllAlarm();
        } catch (Exception e) {
            log.error("get all alarm fail.");
            log.error(e.getMessage(), e);
            throw new WebRequestException(e.getMessage());
        }
    }
}
