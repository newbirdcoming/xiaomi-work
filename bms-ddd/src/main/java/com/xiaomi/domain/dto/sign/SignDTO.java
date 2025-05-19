package com.xiaomi.domain.dto.sign;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignDTO {
    private Long id;
    private String signalData;

    // Factory method to create SignDTO from BatterySignal
    public static SignDTO fromBatterySignal(BatterySignalDTO signal) {
        return SignDTO.builder()
                .id(signal.getId())
                .signalData(signal.toJson())
                .build();
    }
}