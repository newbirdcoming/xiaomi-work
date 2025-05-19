package com.xiaomi.domain.dto.vehicle;


import com.xiaomi.domain.model.vehicle.BatteryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.xiaomi.domain.model.vehicle.BatteryType.fromDescription;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleCreateRequest {
    @NotNull(message = "carId 不能为空")
    private Long carId;

    @NotNull(message = "电池类型不能为空")
    private String batteryType; // 前端传入字符串，后续转换为枚举

    @NotNull(message = "总里程不能为空")
    private BigDecimal totalMileage;

    @NotNull(message = "电池健康状态不能为空")
    private BigDecimal batteryHealth;

    //获取枚举类型
    public BatteryType getBatteryType() {
        return fromDescription(batteryType);
    }
}
