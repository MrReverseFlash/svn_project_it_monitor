package com.envisioniot.uscada.monitor.web.service;

import cn.hutool.core.collection.CollectionUtil;
import com.envisioniot.uscada.monitor.web.dao.rdb.DockerDao;
import com.envisioniot.uscada.monitor.web.dao.tsdb.DockerInfluxDao;
import com.envisioniot.uscada.monitor.web.entity.DockerInfo;
import com.envisioniot.uscada.monitor.web.entity.DockerObj;
import com.envisioniot.uscada.monitor.web.entity.DockerSampleReq;
import com.envisioniot.uscada.monitor.web.entity.DockerSampleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.ONLINE_STATUS;

/**
 * DockerService
 *
 * @author yangkang
 * @date 2021/1/22
 */
@Service
public class DockerService {

    @Autowired
    private DockerDao dockerDao;

    @Resource
    private DockerInfluxDao dockerInfluxDao;

    public List<DockerInfo> qryAll() {
        return dockerDao.qryAll();
    }

    public DockerObj queryAgent(String hostIp) {
        List<DockerInfo> result = dockerDao.query(hostIp);
        DockerObj dockerObj = new DockerObj();
        if (CollectionUtil.isNotEmpty(result)) {
            dockerObj.setIsDocker(true);
            List<String> containerNames = new ArrayList<>();
            result.forEach(dockerInfo -> {
                if (dockerInfo.getStatus() != null && dockerInfo.getStatus() == ONLINE_STATUS) {
                    containerNames.add(dockerInfo.getContainerName());
                }
            });
            dockerObj.setContainerNames(containerNames);
        }
        return dockerObj;
    }


    public List<DockerSampleResp> getDockerSample(DockerSampleReq dockerSampleReq) {
        return dockerInfluxDao.getDockerSample(dockerSampleReq);
    }
}
