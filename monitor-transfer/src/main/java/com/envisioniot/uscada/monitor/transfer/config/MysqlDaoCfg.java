package com.envisioniot.uscada.monitor.transfer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * MysqlDaoCfg
 *
 * @author yangkang
 * @date 2021/5/26
 */
@Configuration
@Conditional(IsMysqlCondition.class)
@ComponentScan(basePackages = {"com.envisioniot.uscada.monitor.transfer.dao.rdb.impl.mysql"},
               includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,
                                                       pattern = ".*")})
public class MysqlDaoCfg {
}
