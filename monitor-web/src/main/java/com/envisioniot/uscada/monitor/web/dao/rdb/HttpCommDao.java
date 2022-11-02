package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.HttpInfo;
import com.envisioniot.uscada.monitor.web.entity.HttpSampleReq;
import com.envisioniot.uscada.monitor.web.mapper.comm.HttpCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HttpCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 11:40
 */
public abstract class HttpCommDao implements HttpDao{

    @Autowired
    protected HttpCommMapper httpCommMapper;

    @Override
    public List<HttpInfo> qryAll() {
        return httpCommMapper.qryAll();
    }

    @Override
    public List<HttpSampleReq> qryHttpSampleReqByIds(List<Long> ids) {
        return httpCommMapper.qryHttpSampleReqByIds(ids);
    }
}
