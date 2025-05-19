package com.xiaomi.domain.factory;


import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.model.vehicle.VehicleInfo;
import com.xiaomi.domain.service.VehicleIdGenerator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class VehicleFactory {

    private final VehicleIdGenerator idGenerator;

    public VehicleFactory(VehicleIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public VehicleInfo createVehicle(Long carId, BatteryType batteryType,
                                     BigDecimal totalMileage, BigDecimal batteryHealth) {
        // 验证必要参数
        if (carId == null || batteryType == null || totalMileage == null || batteryHealth == null) {
            throw new IllegalArgumentException("创建车辆时必要参数不能为空");
        }

        // 生成唯一ID
        String vid = idGenerator.generateVid(carId);

        // 使用Builder模式创建Vehicle实例
        return VehicleInfo.builder()
                .vid(vid)
                .carId(carId)
                .batteryType(batteryType)
                .totalMileage(totalMileage)
                .batteryHealth(batteryHealth)
                .build();
    }
}
