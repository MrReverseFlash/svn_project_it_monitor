package com.envisioniot.uscada.monitor.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * FdbDaoCfg
 *
 * @author yangkang
 * @date 2021/5/26
 */
@Configuration
@Conditional(IsFdbCondition.class)
@ComponentScan(basePackages = {"com.envisioniot.uscada.monitor.web.dao.rdb.impl.fdb"},
               includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,
                                                       pattern = ".*")})
public class FdbDaoCfg {
}
