package com.envisioniot.uscada.monitor.web.service;

import com.envisioniot.uscada.monitor.web.dao.rdb.HttpDao;
import com.envisioniot.uscada.monitor.web.dao.tsdb.HttpInfluxDao;
import com.envisioniot.uscada.monitor.web.entity.DbInfo;
import com.envisioniot.uscada.monitor.web.entity.HttpInfo;
import com.envisioniot.uscada.monitor.web.entity.HttpSampleReq;
import com.envisioniot.uscada.monitor.web.entity.HttpSampleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * HttpService
 *
 * @author yangkang
 * @date 2021/1/28
 */
@Service
public class HttpService {

    @Autowired
    private HttpDao httpDao;

    @Value("${host.matchFlag}")
    private String matchFlag;
    @Resource
    private HttpInfluxDao httpInfluxDao;

    public List<HttpInfo> qryAll() {
        List<HttpInfo> httpInfos = httpDao.qryAll();
        if(!CollectionUtils.isEmpty(httpInfos)) {
            httpInfos.forEach(httpInfo -> httpInfo.setHostMatch(matchFlag.equals(httpInfo.getMatchFlag())));
        }
        return httpInfos;
    }

    public int updateById(HttpInfo httpInfo) {
        return httpDao.updateById(httpInfo);
    }

    public void save(HttpInfo httpInfo) {
        httpDao.save(httpInfo);
    }

    @Transactional(rollbackFor = Exception.class)  //TODO 待优化，没有定义事务管理且没有开启事务注解扫描，所以不生效
    public void deleteByIds(List<Long> ids) {
        List<HttpSampleReq> httpSampleReqList = httpDao.qryHttpSampleReqByIds(ids);
        if (!CollectionUtils.isEmpty(httpSampleReqList)) {
            for (HttpSampleReq httpSampleReq : httpSampleReqList) {
                httpInfluxDao.delHttpHis(httpSampleReq);
            }
            httpDao.deleteByIds(ids);
        }
    }

    public List<HttpSampleResp> getHttpSample(HttpSampleReq httpSampleReq) {
        return httpInfluxDao.getHttpSample(httpSampleReq);
    }
}
