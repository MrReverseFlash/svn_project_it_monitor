package com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.web.dao.rdb.HttpCommDao;
import com.envisioniot.uscada.monitor.web.entity.HttpInfo;
import com.envisioniot.uscada.monitor.web.mapper.fdb.write.HttpFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HttpFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 11:42
 */
public class HttpFdbDao extends HttpCommDao {

    @Autowired
    private HttpFWMapper httpFWMapper;

    @Override
    public int updateById(HttpInfo httpInfo) {
        return httpFWMapper.updateById(httpInfo);
    }

    @Override
    public void save(HttpInfo httpInfo) {
        httpFWMapper.save(httpInfo);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return httpFWMapper.deleteByIds(ids);
    }
}
