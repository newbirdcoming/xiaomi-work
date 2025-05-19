package com.xiaomi.domain.model.vehicle;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class VehicleInfo {
    @TableId("vid")
    private String vid;
    private Long carId;
    private BatteryType batteryType;
    private BigDecimal totalMileage;
    private BigDecimal batteryHealth;


}
