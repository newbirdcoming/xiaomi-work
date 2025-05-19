package com.xiaomi.domain.model.rule;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.vehicle.BatteryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WarningRule {
    private Long id;
    private Integer ruleId;
    private String ruleName;
    private BatteryType batteryType;
    private String conditionExpression;
    private List<RuleCondition> parsedConditions;

    // 解析规则条件
    public void parseConditions() {
        this.parsedConditions = RuleParser.parse(this.conditionExpression);
    }

    // 评估信号是否符合规则
    public Optional<Integer> evaluate(BatterySignalDTO signal){
        if (parsedConditions == null) {
            parseConditions();
        }

        for (RuleCondition condition : parsedConditions) {
            if (condition.matches(signal)) {
                return Optional.of(condition.getLevel());
            }
        }
        return Optional.empty();
    }

    public static class RuleParser {
        // 改进的正则表达式模式
        private static final Pattern CONDITION_PATTERN = Pattern.compile(
                "(?:(?<signalType>电压|电流)差)?\\s*" +  // 可选的信号类型前缀
                        "(?:" +
                        "(?<range>(?<minValue>\\d+(?:\\.\\d+)?)\\s*(?:<=|≥)\\s*\\(?[Mm][Xx]－[Mm][Ii]\\)?\\s*(?:<|＜)\\s*(?<maxValue>\\d+(?:\\.\\d+)?))" +  // 区间模式
                        "|(?<minOnly>(?<minOnlyValue>\\d+(?:\\.\\d+)?)\\s*(?:<=|≥)\\s*\\(?[Mm][Xx]－[Mm][Ii]\\)?)" +  // 大于等于模式
                        "|(?<maxOnly>\\(?[Mm][Xx]－[Mm][Ii]\\)?\\s*(?:<|＜)\\s*(?<maxOnlyValue>\\d+(?:\\.\\d+)?))" +  // 小于模式
                        ")" +
                        "(?:\\s*[，,]\\s*报警等级\\s*[:：]\\s*(?<level>\\d+))?" +  // 可选的报警等级
                        "|(?:(?<noAlertSignalType>电压|电流)差)?\\s*\\(?[Mm][Xx]－[Mm][Ii]\\)?\\s*(?:<|＜)\\s*(?<noAlertValue>\\d+(?:\\.\\d+)?)\\s*[，,]?\\s*不报警"  // 不报警模式
        );

        public static List<RuleCondition> parse(String expression) {
            List<RuleCondition> conditions = new ArrayList<>();

            if (expression == null || expression.trim().isEmpty()) {
                return conditions;
            }

            // 按分号分割多个条件
            String[] rules = expression.split(";");

            for (int i = 0; i < rules.length; i++) {
                String ruleStr = rules[i].trim();
                if (ruleStr.isEmpty()) continue;

                try {
                    RuleCondition rule = parseSingleRule(ruleStr);
                    conditions.add(rule);
                } catch (Exception e) {
                  ;
                }
            }

            return conditions;
        }

        private static RuleCondition parseSingleRule(String ruleStr)  {
            Matcher matcher = CONDITION_PATTERN.matcher(ruleStr);


            RuleCondition rule = new RuleCondition();

            // 处理信号类型 (默认为current)
            String signalType = matcher.group("signalType");
            if (signalType == null) {
                signalType = matcher.group("noAlertSignalType");
            }
            rule.setSignalType(signalType != null && signalType.contains("电压") ? "voltage" : "current");

            // 处理区间条件
            if (matcher.group("range") != null) {
                rule.setMinValue(Double.parseDouble(matcher.group("minValue")));
                rule.setMaxValue(Double.parseDouble(matcher.group("maxValue")));

                rule.setLevel(Integer.parseInt(matcher.group("level")));
            }
            // 处理大于等于条件
            else if (matcher.group("minOnly") != null) {
                rule.setMinValue(Double.parseDouble(matcher.group("minOnlyValue")));
                rule.setMaxValue(null);

                rule.setLevel(Integer.parseInt(matcher.group("level")));
            }
            // 处理小于条件
            else if (matcher.group("maxOnly") != null) {
                rule.setMinValue(null);
                rule.setMaxValue(Double.parseDouble(matcher.group("maxOnlyValue")));
                // 检查是否是不报警的情况
                if (matcher.group("noAlertValue") != null) {
                    rule.setLevel(-1); // 不报警
                } else if (matcher.group("level") != null) {
                    rule.setLevel(Integer.parseInt(matcher.group("level")));
                }
            }
            // 处理不报警条件
            else if (matcher.group("noAlertValue") != null) {
                rule.setMinValue(null);
                rule.setMaxValue(Double.parseDouble(matcher.group("noAlertValue")));
                rule.setLevel(-1); // 不报警等级
            }

            return rule;
        }
    }
}