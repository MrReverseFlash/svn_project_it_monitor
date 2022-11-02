package com.envisioniot.uscada.monitor.web.dao.rdb;

import com.envisioniot.uscada.monitor.web.entity.FeInfo;

import java.util.List;

/**
 * <p>Title: FeDao</p>
 * <p>Description: </p>
 *
 * @author kang.yang2
 * @verion V1.0
 * @date 2022/1/17 15:57
 */
public interface FeDao {

    List<FeInfo> qryAll();
}
