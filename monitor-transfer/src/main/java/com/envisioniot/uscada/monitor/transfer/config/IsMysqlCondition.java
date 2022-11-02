package com.envisioniot.uscada.monitor.transfer.config;

import com.envisioniot.uscada.monitor.common.enums.DBType;
import com.envisioniot.uscada.monitor.common.util.CommUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * IsMysqlCondition
 *
 * @author yangkang
 * @date 2021/5/26
 */
public class IsMysqlCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        if (CommUtil.getDB_TYPE() == DBType.MYSQL) {
            return true;
        }
        return false;
    }
}
