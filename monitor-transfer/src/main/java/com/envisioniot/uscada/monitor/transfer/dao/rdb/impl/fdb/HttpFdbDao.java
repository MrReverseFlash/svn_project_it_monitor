package com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.fdb;

import com.envisioniot.uscada.monitor.common.entity.HttpStat;
import com.envisioniot.uscada.monitor.transfer.dao.rdb.HttpCommDao;
import com.envisioniot.uscada.monitor.transfer.mapper.fdb.write.HttpFWMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: HttpFdbDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:24
 */
public class HttpFdbDao extends HttpCommDao {

    @Autowired
    private HttpFWMapper httpFWMapper;

    @Override
    public void insertBatch(String hostIp, List<HttpStat> list) {
        httpFWMapper.insertBatch(hostIp, list);
    }

    @Override
    public void batchForUpdate(String hostIp, List<HttpStat> list) {
        httpFWMapper.batchForUpdate_update_first(hostIp, list);
        httpFWMapper.batchForUpdate_insert_later(hostIp, list);
    }
}
