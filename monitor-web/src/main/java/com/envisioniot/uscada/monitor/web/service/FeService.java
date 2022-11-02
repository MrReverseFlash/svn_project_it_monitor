package com.envisioniot.uscada.monitor.web.service;

import com.envisioniot.uscada.monitor.web.dao.rdb.FeDao;
import com.envisioniot.uscada.monitor.web.dao.tsdb.FeInfluxDao;
import com.envisioniot.uscada.monitor.web.entity.FeInfo;
import com.envisioniot.uscada.monitor.web.entity.FeSampleReq;
import com.envisioniot.uscada.monitor.web.entity.FeSampleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * FeService
 *
 * @author yangkang
 * @date 2021/1/27
 */
@Service
public class FeService {

    @Autowired
    private FeDao feDao;

    @Resource
    private FeInfluxDao feInfluxDao;

    public List<FeInfo> qryAll() {
        return feDao.qryAll();
    }

    public List<FeSampleResp> getFeSample(FeSampleReq feSampleReq) {
        return feInfluxDao.getFeSample(feSampleReq);
    }

}
