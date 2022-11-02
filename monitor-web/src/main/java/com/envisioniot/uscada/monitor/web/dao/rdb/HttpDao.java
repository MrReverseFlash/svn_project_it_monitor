package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.HttpInfo;
import com.envisioniot.uscada.monitor.web.entity.HttpSampleReq;

import java.util.List;

/**
 * <p>Title: HttpDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 11:39
 */
public interface HttpDao {

    List<HttpInfo> qryAll();

    int updateById(HttpInfo httpInfo);

    void save(HttpInfo httpInfo);

    int deleteByIds(List<Long> ids);

    List<HttpSampleReq> qryHttpSampleReqByIds(List<Long> ids);
}
