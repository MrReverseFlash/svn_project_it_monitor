package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.DockerInfo;

import java.util.List;

/**
 * <p>Title: DockerDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 15:49
 */
public interface DockerDao {
    List<DockerInfo> qryAll();

    List<DockerInfo> query(String hostIp);
}
