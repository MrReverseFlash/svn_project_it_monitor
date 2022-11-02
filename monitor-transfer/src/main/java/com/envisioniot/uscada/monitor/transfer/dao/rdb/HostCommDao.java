package com.envisioniot.uscada.monitor.transfer.dao.rdb;

import com.envisioniot.uscada.monitor.transfer.mapper.comm.HostCommMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.envisioniot.uscada.monitor.common.util.CommConstants.OFFLINE_STATUS;

/**
 * <p>Title: HostCommDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/13 16:03
 */
public abstract class HostCommDao implements HostDao{

    @Autowired
    protected HostCommMapper hostCommMapper;

    @Override
    public String qryHostNameByIp(String hostIp) {
        return hostCommMapper.qryHostNameByIp(hostIp);
    }


    public List<String> queryAllOnlineHostIps(String matchFlag){
        return hostCommMapper.queryAllOnlineHostIps(matchFlag);
    }

}
