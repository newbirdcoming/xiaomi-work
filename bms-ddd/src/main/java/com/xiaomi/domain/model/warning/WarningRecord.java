package com.xiaomi.domain.model.warning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarningRecord {
    private Long id;
    private Long carId;
    private Integer ruleId;
    private String warnName;
    private Integer warnLevel;
    private LocalDateTime createTime;
    private String signalData;
}
