package com.xiaomi.application.service;


import com.xiaomi.domain.dto.warning.WarningRequest;
import com.xiaomi.domain.factory.VehicleFactory;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.repository.WarningRecordRepository;
import com.xiaomi.domain.repository.WarningRuleRepository;
import com.xiaomi.domain.service.WarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarningRuleApplicationService {
    private final WarningRuleRepository warningRuleRepository;
    // 要求：根据规则编号获取规则，然后解析返回
    public List<WarningRule> getWarningRuleByRuleId(Integer ruleId){
        // 1. 获取规则
        List<WarningRule> byRuleId = warningRuleRepository.findByRuleId(ruleId);
        //2.使用内部方法获取规则解析
        try {
            byRuleId.forEach(rule -> {
                rule.parseConditions();
                System.out.println(rule.getParsedConditions());
            });
        } catch (Exception e) {
            return null;
        }
        //3.返回
        return byRuleId;
    }




}
