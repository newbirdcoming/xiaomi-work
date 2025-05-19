package com.xiaomi.bmsddd.rule;

import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.model.warning.WarningRecord;
import com.xiaomi.domain.repository.WarningRecordRepository;
import com.xiaomi.domain.repository.WarningRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class Test {


    @Autowired
    private WarningRuleRepository warningRuleRepository;
//    插入规则
    @org.junit.jupiter.api.Test
    void testinsert(){
        WarningRule record = new WarningRule();
        record.setRuleId(1);
        record.setRuleName("电压差报警");
        record.setBatteryType(BatteryType.LITHIUM_IRON);
        record.setConditionExpression("5<=(Mx - Mi),报警等级: 0;3<=(Mx - Mi)<5,报警等级: 1;1<=(Mx - Mi)<3,报警等级: 2;0.6<=(Mx - Mi)<1,报警等级: 3;0.2<=(Mx - Mi)<0.6,报警等级: 4;(Mx - Mi)<0.2, 不报警");
        warningRuleRepository.insert(record);
    }

//    根据规则id获取规则
    @org.junit.jupiter.api.Test
    void testgetByRuleId(){
        List<WarningRule> byRuleId = warningRuleRepository.findByRuleId(1);
        for (WarningRule warningRule : byRuleId) {
            log.info("规则:         :{}",warningRule);
        }
    }

// 根据类型获取规则
    @org.junit.jupiter.api.Test
    void testgetByBatteryType(){
        System.out.println(BatteryType.LITHIUM_IRON.getDescription());
        List<WarningRule> byBatteryType = warningRuleRepository.findByBatteryType(BatteryType.LITHIUM_IRON.getDescription());
        for (WarningRule warningRule : byBatteryType) {
            log.info("规则:         :{}",warningRule);
        }
    }




}
