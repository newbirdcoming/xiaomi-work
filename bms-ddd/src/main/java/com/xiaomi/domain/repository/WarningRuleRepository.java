package com.xiaomi.domain.repository;

import com.xiaomi.domain.dto.warning.WarningRequest;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.service.WarningService;
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

    /**
     * 根据carId获取电池类型
     */
    String getBatteryTypeByCarId(Long carId);


//    List<WarningRule> findByCarId(Long carId);
}
