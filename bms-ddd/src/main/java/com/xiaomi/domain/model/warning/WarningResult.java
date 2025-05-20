package com.xiaomi.domain.model.warning;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class WarningResult {
    Long carId;
    String batteryType;
    String warnName;
    Integer warnLevel;
}