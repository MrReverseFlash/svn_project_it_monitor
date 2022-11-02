package com.envisioniot.uscada.monitor.transfer.config;

import com.envisioniot.uscada.monitor.common.enums.DBType;
import com.envisioniot.uscada.monitor.common.util.CommUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * IsFdbCondition
 *
 * @author yangkang
 * @date 2021/5/26
 */
public class IsFdbCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        if (CommUtil.getDB_TYPE() == DBType.FDB) {
            return true;
        }
        return false;
    }
}
