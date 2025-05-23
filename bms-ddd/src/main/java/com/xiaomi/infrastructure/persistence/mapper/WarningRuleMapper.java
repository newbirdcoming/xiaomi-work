package com.xiaomi.infrastructure.persistence.mapper;

import com.xiaomi.domain.dto.warning.WarningRequest;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.warning.WarningResult;
import com.xiaomi.domain.service.WarningService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarningRuleMapper {
    List<WarningRule> getByRuleId(Integer id);

    List<WarningRule> findByBatteryType(String description);

    int insert(WarningRule warningRule);

    List<WarningResult> processWarnings(WarningRequest request);

    String getBatteryTypeByCarId(Long carId);


}
