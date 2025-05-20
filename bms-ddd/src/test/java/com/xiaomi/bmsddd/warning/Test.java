package com.xiaomi.bmsddd.warning;

import com.xiaomi.domain.dto.warning.WarningRequest;
import com.xiaomi.domain.model.rule.RuleCondition;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.warning.WarningResult;
import com.xiaomi.domain.service.WarningService;
import com.xiaomi.interfaces.controller.WarningController;
import com.xiaomi.interfaces.vo.ResponseResult;
import io.lettuce.core.ScriptOutputType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class Test {
    private WarningRule rule;

    @Autowired
    WarningController warningController;

    @org.junit.jupiter.api.Test
    void testMatchVoltageCondition() {
        // 创建测试数据
        List<WarningRequest> requests = Arrays.asList(
                new WarningRequest(111L, 1, "{\"Mx\":12.0,\"Mi\":0.6}"),
                new WarningRequest(111L, 1, "{\"Mx\":12.0,\"Mi\":0.6}"),
                new WarningRequest(111L, 1, "{\"Mx\":12.0,\"Mi\":0.6}"),
                new WarningRequest(111L, 1, "{\"Mx\":12.0,\"Mi\":0.6}")
        );

        // 调用被测试方法
        ResponseResult<List<WarningResult>> responses = warningController.processWarnings(requests);
        responses.getData().forEach(System.out::println);
    }
}



//    @org.junit.jupiter.api.Test
//    @BeforeEach
//    void setUp() {
//        // 初始化规则：电压差[3.0,5.0)等级1，电流差<0.2等级2
//        String ruleExpression = "3.0<=(Mx - Mi)<5.0, 报警等级:1; (Mx - Mi)<0.2, 报警等级:2";
//        rule = WarningRule.builder()
//                .conditionExpression(ruleExpression)
//                .build();
//        rule.parseConditions(); // 提前解析规则
//    }
////    5<=(Mx - Mi),报警等级: 0;3<=(Mx - Mi)<5,报警等级: 1;1<=(Mx - Mi)<3,报警等级: 2;0.6<=(Mx - Mi)<1,报警等级: 3;0.2<=(Mx - Mi)<0.6,报警等级: 4;(Mx - Mi)<0.2, 不报警
//
//    // 1. 测试匹配电压规则
//    @org.junit.jupiter.api.Test
//    void testMatchVoltageCondition() {
//        BatterySignalDTO signal = BatterySignalDTO.builder()
//                .mx(10.0)  // Mx - Mi = 10.0 - 6.5 = 3.5 ∈ [3.0,5.0)
//                .mi(6.5)
//                .ix(8.0)    // Ix - Ii = 0.3 (不匹配)
//                .ii(7.7)
//                .build();
//
//        Optional<Integer> result = rule.evaluate(signal);
//        System.out.println("1111111111"+result.get());
//    }
//
//    // 2. 测试匹配电流规则
//    @org.junit.jupiter.api.Test
//    void testMatchCurrentCondition() {
//        BatterySignalDTO signal = BatterySignalDTO.builder()
//                .mx(10.0)  // Mx - Mi = 1.0 (不匹配)
//                .mi(9.0)
//                .ix(5.0)   // Ix - Ii = 0.1 < 0.2
//                .ii(4.9)
//                .build();
//
//        Optional<Integer> result = rule.evaluate(signal);
//        System.out.println("2222222222222222"+result.get());
//    }
//
//    // 3. 测试短路逻辑（同时匹配时优先返回第一条）
//    @org.junit.jupiter.api.Test
//    void testShortCircuitEvaluation() {
//        BatterySignalDTO signal = BatterySignalDTO.builder()
//                .mx(10.0)  // Mx - Mi = 3.5 ∈ [3.0,5.0)
//                .mi(6.5)
//                .ix(5.0)   // Ix - Ii = 0.1 < 0.2
//                .ii(4.9)
//                .build();
//
//        Optional<Integer> result = rule.evaluate(signal);
//        System.out.println("3333333333333333"+result.get());
//    }
//
//    // 4. 测试不匹配任何规则
//    @org.junit.jupiter.api.Test
//    void testNoMatch() {
//        BatterySignalDTO signal = BatterySignalDTO.builder()
//                .mx(10.0)  // Mx - Mi = 1.0 (不匹配)
//                .mi(9.0)
//                .ix(5.0)   // Ix - Ii = 0.3 (不匹配)
//                .ii(4.7)
//                .build();
//
//        Optional<Integer> evaluate = rule.evaluate(signal);
//        System.out.println("4444444444444444"+evaluate.get());
//        if(evaluate.isPresent()){
//            System.out.println(evaluate.get());
//        }
//        else
//            System.out.println("No match");
//    }
//
//    // 5. 测试边界值（电压差刚好等于3.0）
//    @org.junit.jupiter.api.Test
//    void testVoltageBoundaryMin() {
//        BatterySignalDTO signal = BatterySignalDTO.builder()
//                .mx(10.0)  // Mx - Mi = 3.0 (等于最小值)
//                .mi(7.0)
//                .ix(0.0)   // 无关值
//                .ii(0.0)
//                .build();
//
//        Optional<Integer> result = rule.evaluate(signal);
//        System.out.println("5555555555555555555"+result.get());
//    }
//
//    // 6. 测试空值处理
//    @org.junit.jupiter.api.Test
//    void testNullValueHandling() {
//        BatterySignalDTO signal = BatterySignalDTO.builder()
//                .mx(null)  // 电压为null
//                .mi(7.0)
//                .ix(5.0)
//                .ii(4.9)
//                .build();
//
//    }

