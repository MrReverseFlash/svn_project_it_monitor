package com.envisioniot.uscada.monitor.web.service;

import com.envisioniot.uscada.monitor.web.dao.rdb.PortDao;
import com.envisioniot.uscada.monitor.web.dao.tsdb.PortInfluxDao;
import com.envisioniot.uscada.monitor.web.entity.HttpInfo;
import com.envisioniot.uscada.monitor.web.entity.PortInfo;
import com.envisioniot.uscada.monitor.web.entity.PortSampleReq;
import com.envisioniot.uscada.monitor.web.entity.PortSampleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * PortService
 *
 * @author yangkang
 * @date 2021/1/21
 */
@Service
public class PortService {

    @Autowired
    private PortDao portDao;

    @Value("${host.matchFlag}")
    private String matchFlag;

    @Resource
    private PortInfluxDao portInfluxDao;

    public List<PortInfo> qryAll(){
        List<PortInfo> portInfos = portDao.qryAll();
        if(!CollectionUtils.isEmpty(portInfos)) {
            portInfos.forEach(portInfo -> portInfo.setHostMatch(matchFlag.equals(portInfo.getMatchFlag())));
        }
        return portInfos;
    }

    public int updateById(PortInfo portInfo) {
        return portDao.updateById(portInfo);
    }

    public void save(PortInfo portInfo) {
        portDao.save(portInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        List<PortSampleReq> portSampleReqList = portDao.qryPortSampleReqByIds(ids);
        if (!CollectionUtils.isEmpty(portSampleReqList)) {
            for (PortSampleReq portSampleReq : portSampleReqList) {
                portInfluxDao.delPortHis(portSampleReq);
            }
            portDao.deleteByIds(ids);
        }
    }

    public List<PortSampleResp> getPortSample(PortSampleReq portSampleReq) {
        return portInfluxDao.getPortSample(portSampleReq);
    }
}
