package com.xiaomi.infrastructure.persistence.mapper;

import com.xiaomi.domain.model.rule.WarningRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarningRuleMapper {
    List<WarningRule> getByRuleId(Integer id);

    List<WarningRule> findByBatteryType(String description);

    int insert(WarningRule warningRule);
}
