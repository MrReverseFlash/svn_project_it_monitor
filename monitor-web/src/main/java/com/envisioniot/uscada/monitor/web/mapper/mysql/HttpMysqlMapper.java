package com.envisioniot.uscada.monitor.web.mapper.mysql;

import com.envisioniot.uscada.monitor.web.entity.HttpInfo;

import java.util.List;

/**
 * HttpMysqlMapper
 *
 * @author yangkang
 * @date 2021/1/28
 */
public interface HttpMysqlMapper {

    int updateById(HttpInfo httpInfo);

    void save(HttpInfo httpInfo);

    int deleteByIds(List<Long> ids);
}
