package com.xiaomi.domain.repository;

import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.vehicle.BatteryType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface WarningRuleRepository {

    /**
     * 根据规则id查询告警规则
     * @param ruleId
     * @return
     */
    List<WarningRule> findByRuleId(Integer ruleId);

    /**
     * 根据电池类型查询告警规则
     * @param batteryType
     * @return
     */
    List<WarningRule> findByBatteryType(String batteryType);

    /**
     * 插入规则
     */
    int insert(WarningRule warningRule);


}
