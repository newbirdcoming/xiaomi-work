package com.xiaomi.application.service;

import com.xiaomi.domain.dto.vehicle.VehicleCreateRequest;
import com.xiaomi.domain.dto.vehicle.VehicleResponse;
import com.xiaomi.domain.factory.VehicleFactory;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.model.vehicle.VehicleInfo;
import com.xiaomi.domain.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleApplicationService {

    private final VehicleFactory vehicleFactory;
    private final VehicleRepository vehicleRepository;

    public VehicleApplicationService(VehicleFactory vehicleFactory,
                                     VehicleRepository vehicleRepository) {
        this.vehicleFactory = vehicleFactory;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public int createVehicle(VehicleCreateRequest vehicleCreateRequest) {
        // 获取枚举类型
        BatteryType type = vehicleCreateRequest.getBatteryType();

        // 使用工厂创建领域对象
        VehicleInfo vehicle = vehicleFactory.createVehicle(
                vehicleCreateRequest.getCarId(), type, vehicleCreateRequest.getTotalMileage(),vehicleCreateRequest.getBatteryHealth());
        // 保存到数据库
        return vehicleRepository.save(vehicle);
    }


    public Optional<VehicleResponse> findByCarId(Long carId) {
        Optional<VehicleInfo> byCarId = vehicleRepository.findByCarId(carId);
        // 转成输出格式
        if(byCarId.isPresent()){
            VehicleInfo vehicleInfo = byCarId.get();
            return Optional.of(new VehicleResponse(vehicleInfo));
        }
        else
            return Optional.empty();
    }


}