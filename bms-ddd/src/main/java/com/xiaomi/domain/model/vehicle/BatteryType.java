package com.xiaomi.domain.model.vehicle;

public enum BatteryType {
    LITHIUM_ION("三元电池"),
    LITHIUM_IRON("铁锂电池");

    private final String description;

    BatteryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


//字符串转枚举
    public static BatteryType fromDescription(String description) {
        // 遍历BatteryType枚举类型的所有值
        for (BatteryType type : BatteryType.values()) {
            // 如果当前枚举类型的描述与传入的描述相等
            if (type.getDescription().equals(description)) {
                // 返回当前枚举类型
                return type;
            }
        }
        // 如果没有找到匹配的枚举类型，抛出异常
        throw new IllegalArgumentException("No enum constant for description: " + description);
    }
}