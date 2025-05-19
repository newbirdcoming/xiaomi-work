package com.xiaomi.infrastructure.persistence.repository;

import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.repository.WarningRuleRepository;
import com.xiaomi.infrastructure.persistence.mapper.WarningRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class WarningRuleRepositoryImpl implements WarningRuleRepository {
    @Autowired
    private WarningRuleMapper warningRuleMapper;

    @Override
    public List<WarningRule> findByBatteryType(String batteryType) {
        List<WarningRule> byBatteryType = warningRuleMapper.findByBatteryType(batteryType);
        return byBatteryType;
    }

    @Transactional
    @Override
    public int insert(WarningRule warningRule) {
        int res=warningRuleMapper.insert(warningRule);
        return res;
    }

    @Override
    public List<WarningRule> findByRuleId(Integer ruleId) {
        List<WarningRule> byRuleId = warningRuleMapper.getByRuleId(ruleId);
        return byRuleId;
    }
}
