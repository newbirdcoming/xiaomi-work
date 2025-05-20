package com.xiaomi.domain.model.rule;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Slf4j
public class RuleCondition {
    private Double minValue;
    private Double maxValue;
    private Integer level;
    private String signalType;//"voltage"（电压）和 "current"（电流）

    public boolean matches(BatterySignalDTO signal) {
        // 1. 检查信号对象是否为空
        if (signal == null) {
            log.debug("信号对象为空，匹配失败");
            return false;
        }

        // 2. 检查信号类型是否有效
        if (!"voltage".equals(signalType) && !"current".equals(signalType)) {
            log.warn("无效的信号类型: {}", signalType);
            return false;
        }

        // 3. 根据信号类型获取并验证差值
        double diff;
        if ("voltage".equals(signalType)) {
            if (signal.getMx() == null || signal.getMi() == null) {
                log.debug("电压信号数据不完整: Mx={}, Mi={}", signal.getMx(), signal.getMi());
                return false;
            }
            diff = signal.getMx() - signal.getMi();
        } else { // current
            if (signal.getIx() == null || signal.getIi() == null) {
                log.debug("电流信号数据不完整: Ix={}, Ii={}", signal.getIx(), signal.getIi());
                return false;
            }
            diff = signal.getIx() - signal.getIi();
        }

        // 4. 计算差值是否在范围内
        boolean minCondition = minValue == null || diff >= minValue;
        boolean maxCondition = maxValue == null || diff < maxValue;

        // 5. 记录匹配结果（可选，调试时使用）
        if (log.isDebugEnabled()) {
            log.debug("信号差值={}, 条件范围=[{}, {}), 匹配结果={}",
                    diff, minValue, maxValue, minCondition && maxCondition);
        }

        return minCondition && maxCondition;
    }
}