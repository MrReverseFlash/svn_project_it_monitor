package com.envisioniot.uscada.monitor.web.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.web.dao.rdb.HttpCommDao;
import com.envisioniot.uscada.monitor.web.entity.HttpInfo;
import com.envisioniot.uscada.monitor.web.mapper.mysql.HttpMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HttpMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/18 11:41
 */
public class HttpMysqlDao extends HttpCommDao {

    @Autowired
    private HttpMysqlMapper httpMysqlMapper;

    @Override
    public int updateById(HttpInfo httpInfo) {
        return httpMysqlMapper.updateById(httpInfo);
    }

    @Override
    public void save(HttpInfo httpInfo) {
        httpMysqlMapper.save(httpInfo);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return httpMysqlMapper.deleteByIds(ids);
    }
}
