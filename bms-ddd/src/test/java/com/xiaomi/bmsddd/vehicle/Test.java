package com.xiaomi.bmsddd.vehicle;

import com.xiaomi.application.service.VehicleApplicationService;
import com.xiaomi.domain.dto.vehicle.VehicleCreateRequest;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.model.vehicle.VehicleInfo;
import com.xiaomi.infrastructure.persistence.handler.BatteryTypeHandler;
import com.xiaomi.infrastructure.persistence.mapper.VehicleMapper;
import com.xiaomi.interfaces.controller.VehicleController;
import com.xiaomi.interfaces.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Slf4j
@SpringBootTest
public class Test {
    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private VehicleApplicationService vehicleApplicationService;

    // -------------------------------------要求1--------------------------------------- 测试插入
    @org.junit.jupiter.api.Test
    void saveVehicleInfoTest(){
        VehicleInfo vehicle = VehicleInfo.builder()
                .vid("V000023")
                .carId(2232L)
                .batteryType(BatteryType.LITHIUM_ION)  // Java代码中使用枚举
                .totalMileage(new BigDecimal("10000.50"))
                .batteryHealth(new BigDecimal("95.00"))
                .build();
        int save = vehicleMapper.save(vehicle);
        if (save>0){
            log.info("保存成功");
        }else
            log.info("保存失败");
    }

    // 测试查询
    @org.junit.jupiter.api.Test
    void testSelect(){
        // 从数据库查询时，MyBatis会自动将"三元电池"转换为BatteryType.LITHIUM_ION
        VehicleInfo result = vehicleMapper.findByCarId(1234567890L);
        BatteryType type = result.getBatteryType();  // 返回 BatteryType.LITHIUM_ION
        System.out.println(type);
    }


}
