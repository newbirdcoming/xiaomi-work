package com.xiaomi.domain.model.rule;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RuleCondition {
    private Double minValue;
    private Double maxValue;
    private Integer level;
    private String signalType; // "voltage" or "current"

    public boolean matches(BatterySignalDTO signal) {
        if (signal == null) {
            return false;
        }

        double diff;
        if ("voltage".equals(signalType)) {
            if (signal.getMx() == null || signal.getMi() == null) {
                return false;
            }
            diff = signal.getMx() - signal.getMi();
        } else { // current
            if (signal.getIx() == null || signal.getIi() == null) {
                return false;
            }
            diff = signal.getIx() - signal.getIi();
        }

        boolean minCondition = minValue == null || diff >= minValue;
        boolean maxCondition = maxValue == null || diff < maxValue;

        return minCondition && maxCondition;
    }
}