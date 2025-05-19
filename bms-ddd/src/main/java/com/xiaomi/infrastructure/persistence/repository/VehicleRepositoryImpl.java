package com.xiaomi.infrastructure.persistence.repository;

import com.xiaomi.domain.model.vehicle.VehicleInfo;
import com.xiaomi.domain.repository.VehicleRepository;
import com.xiaomi.infrastructure.persistence.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class VehicleRepositoryImpl implements VehicleRepository {

    @Autowired
    private VehicleMapper vehicleMapper;
    @Override
    public Optional<VehicleInfo> findByCarId(Long carId) {
        VehicleInfo vehicleInfo=vehicleMapper.findByCarId(carId);
        return Optional.ofNullable(vehicleInfo);
    }

    @Override
    public int save(VehicleInfo vehicle) {
        return vehicleMapper.save(vehicle);
    }
}
