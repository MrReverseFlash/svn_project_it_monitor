package com.envisioniot.uscada.monitor.web.mapper.fdb.write;

import com.envisioniot.uscada.monitor.web.entity.HttpInfo;

import java.util.List;

/**
 * HttpFWMapper
 *
 * @author yangkang
 * @date 2021/1/28
 */
public interface HttpFWMapper {

    int updateById(HttpInfo httpInfo);

    void save(HttpInfo httpInfo);

    int deleteByIds(List<Long> ids);
}
