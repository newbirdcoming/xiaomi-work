package com.xiaomi.domain.repository;

import com.xiaomi.domain.model.vehicle.VehicleInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface VehicleRepository {
    Optional<VehicleInfo> findByCarId(@Param("carId") Long carId);
    int save(VehicleInfo vehicle);

}