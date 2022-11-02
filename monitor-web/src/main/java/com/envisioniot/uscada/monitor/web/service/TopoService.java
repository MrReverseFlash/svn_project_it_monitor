package com.envisioniot.uscada.monitor.web.service;

import com.envisioniot.uscada.monitor.common.entity.TopoParam;
import com.envisioniot.uscada.monitor.web.dao.rdb.TopoDao;
import com.envisioniot.uscada.monitor.web.entity.GatewayInfo;
import com.envisioniot.uscada.monitor.web.entity.TopoInfo;
import com.envisioniot.uscada.monitor.web.entity.TopoRela;
import com.envisioniot.uscada.monitor.web.exception.WebRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * TopoService
 *
 * @author yangkang
 * @date 2021/3/2
 */
@Service
public class TopoService {

    @Autowired
    private TopoDao topoDao;

    public List<TopoInfo> qryTopoInfo(){
        List<TopoInfo> topoInfos = topoDao.qryTopoInfo();
        if (!CollectionUtils.isEmpty(topoInfos)) {
            topoInfos.parallelStream().forEach(topoInfo -> {
                String hostIp = topoInfo.getHostIp();
                List<GatewayInfo> gatewayInfos = topoDao.qryGatewayByHost(hostIp);
                if (!CollectionUtils.isEmpty(gatewayInfos)) {
                    topoInfo.setGatewayList(gatewayInfos);
                }
                List<TopoRela> topoRelas = topoDao.qryTopoRelaByHost(hostIp);
                if (!CollectionUtils.isEmpty(topoRelas)) {
                    topoInfo.setTopoRelaList(topoRelas);
                }
            });
        }
        return topoInfos;
    }

    public void insCustRelaByHost(String hostIpA, String hostIpB) {
        topoDao.insCustRelaByHost(hostIpA, hostIpB);
    }

    public void delCustRelaByHost(String hostIpA, String hostIpB) {
        topoDao.delCustRelaByHost(hostIpA, hostIpB);
    }

    public List<TopoParam> qryTopoParamByType(short type) {
        return topoDao.qryTopoParamByType(type);
    }

    public String insTopoParam(short num, String name, short type) {
        TopoParam param = new TopoParam();
        param.setNum(num);
        param.setName(name);
        param.setType(type);
        param.setId(UUID.randomUUID().toString());
        topoDao.insTopoParam(param);
        return param.getId();
    }

    public void updTopoParam(String id, Short num, String name) {
        TopoParam param = new TopoParam();
        param.setId(id);
        param.setNum(num);
        param.setName(name);
        topoDao.updTopoParam(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updHostParamId(String id, short type, List<String> list) {
        topoDao.updHostParamId(id, type, list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updHostCoordinate(List<TopoInfo> list) {
        topoDao.updHostCoordinate(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delHostParamId(short type, List<String> list) {
        topoDao.delHostParamId(type, list);
    }

    public void delTopoParam(String id, short type) {
        String existHost = topoDao.qryHostByParamId(id, type);
        if (!StringUtils.isEmpty(existHost)) {
            throw new WebRequestException("还有主机绑定了该参数，删除前请先解绑！");
        }
        topoDao.delTopoParam(id);
    }
}
