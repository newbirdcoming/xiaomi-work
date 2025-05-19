package com.xiaomi.interfaces.exception;



public class RuleNotFoundException extends RuntimeException {
    private final Integer ruleId;

    public RuleNotFoundException(Integer ruleId) {
        super("预警规则不存在: " + ruleId);
        this.ruleId = ruleId;
    }

    public Integer getRuleId() {
        return ruleId;
    }
}
