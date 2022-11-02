package com.envisioniot.uscada.monitor.web.mapper.comm;

import com.envisioniot.uscada.monitor.web.entity.FeInfo;

import java.util.List;

/**
 * FeCommMapper
 *
 * @author yangkang
 * @date 2021/1/27
 */
public interface FeCommMapper {

    List<FeInfo> qryAll();
}
