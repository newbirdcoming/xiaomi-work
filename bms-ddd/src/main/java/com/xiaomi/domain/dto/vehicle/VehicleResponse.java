package com.xiaomi.domain.dto.vehicle;

import com.xiaomi.domain.model.vehicle.VehicleInfo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleResponse {
    private String vid;
    private Long carId;
    private String batteryType; // 将 BatteryType 转为 String 类型
    private BigDecimal totalMileage;
    private BigDecimal batteryHealth;

    // 构造函数
    public VehicleResponse(VehicleInfo vehicleInfo) {
        this.vid = vehicleInfo.getVid();
        this.carId = vehicleInfo.getCarId();
        this.batteryType = vehicleInfo.getBatteryType() != null ?
                vehicleInfo.getBatteryType().getDescription() : null;
        this.totalMileage = vehicleInfo.getTotalMileage();
        this.batteryHealth = vehicleInfo.getBatteryHealth();
    }
}
