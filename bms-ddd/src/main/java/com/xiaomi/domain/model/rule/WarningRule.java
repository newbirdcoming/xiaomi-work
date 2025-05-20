package com.xiaomi.domain.model.rule;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.interfaces.exception.DMSException;
import com.xiaomi.interfaces.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaomi.interfaces.exception.ExceptionType.RULE_PARSE_FAIL;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Slf4j
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
        // 修正后的正则表达式模式
        private static final Pattern CONDITION_PATTERN = Pattern.compile(
                "(?:(?<minValue>\\d+(?:\\.\\d+)?)\\s*<=\\s*\\(?[Mm][xX]\\s*-\\s*[Mm][iI]\\)?" +
                        "(?:\\s*<\\s*(?<maxValue>\\d+(?:\\.\\d+)?))?)" +
                        "|(?:\\(?[Mm][xX]\\s*-\\s*[Mm][iI]\\)?\\s*<\\s*(?<maxOnly>\\d+(?:\\.\\d+)?))" +
                        "\\s*[,，]\\s*(?:报警等级\\s*[:：]\\s*)?(?<level>\\d+|不报警)"
        );

        public static List<RuleCondition> parse(String expression) {
            List<RuleCondition> conditions = new ArrayList<>();

            if (expression == null || expression.trim().isEmpty()) {
                return conditions;
            }

            String[] rules = expression.split(";");

            for (String ruleStr : rules) {
                ruleStr = ruleStr.trim();
                if (ruleStr.isEmpty()) continue;

                try {
                    RuleCondition rule = parseSingleRule(ruleStr);
                    if (rule != null) {
                        conditions.add(rule);
                    }
                } catch (Exception e) {
                    log.info("规则解析失败 "+ ruleStr);
                    throw new DMSException(RULE_PARSE_FAIL.getErrorCode(), RULE_PARSE_FAIL.getErrorMessage()+e.getMessage());
                }
            }

            return conditions;
        }

        private static RuleCondition parseSingleRule(String ruleStr) {
            Matcher matcher = CONDITION_PATTERN.matcher(ruleStr);
            if (!matcher.find()) {
                System.err.println("Pattern not matched: " + ruleStr);
                return null;
            }

            RuleCondition rule = new RuleCondition();
            rule.setSignalType("current"); // 默认为电流

            // 处理区间条件 (如 "3<=(Mx - Mi)<5")
            if (matcher.group("minValue") != null) {
                rule.setMinValue(Double.parseDouble(matcher.group("minValue")));
                rule.setMaxValue(matcher.group("maxValue") != null ?
                        Double.parseDouble(matcher.group("maxValue")) : null);
            }
            // 处理单边条件 (如 "(Mx - Mi)<0.2")
            else if (matcher.group("maxOnly") != null) {
                rule.setMinValue(null);
                rule.setMaxValue(Double.parseDouble(matcher.group("maxOnly")));
            }

            // 处理报警等级
            String levelStr = matcher.group("level");
            if (levelStr != null) {
                if ("不报警".equals(levelStr)) {
                    rule.setLevel(-1);
                } else {
                    try {
                        rule.setLevel(Integer.parseInt(levelStr));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid level format: " + levelStr);
                        rule.setLevel(0); // 默认值
                    }
                }
            } else {
                rule.setLevel(0); // 默认值
            }

            return rule;
        }
    }
}