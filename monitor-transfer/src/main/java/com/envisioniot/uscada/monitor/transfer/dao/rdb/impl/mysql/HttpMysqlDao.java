package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql;

import com.envisioniot.uscada.monitor.common.entity.HttpStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.HttpCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.mysql.HttpMysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HttpMysqlDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:23
 */
public class HttpMysqlDao extends HttpCommDao {

    @Autowired
    private HttpMysqlMapper httpMysqlMapper;

    @Override
    public void insertBatch(String hostIp, List<HttpStat> list) {
        httpMysqlMapper.insertBatch(hostIp, list);
    }

    @Override
    public void batchForUpdate(String hostIp, List<HttpStat> list) {
        httpMysqlMapper.batchForUpdate(hostIp, list);
    }
}
