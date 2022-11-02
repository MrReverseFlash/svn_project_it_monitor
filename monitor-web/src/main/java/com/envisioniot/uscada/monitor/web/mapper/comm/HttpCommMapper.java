package com.envisioniot.uscada.monitor.web.mapper.comm;

import com.envisioniot.uscada.monitor.web.entity.HttpInfo;
import com.envisioniot.uscada.monitor.web.entity.HttpSampleReq;

import java.util.List;

/**
 * HttpCommMapper
 *
 * @author yangkang
 * @date 2021/1/28
 */
public interface HttpCommMapper {

    List<HttpInfo> qryAll();

    List<HttpSampleReq> qryHttpSampleReqByIds(List<Long> ids);
}
