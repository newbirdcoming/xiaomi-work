package com.xiaomi.infrastructure.persistence.mapper;

import com.xiaomi.domain.model.vehicle.VehicleInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VehicleMapper {


    VehicleInfo findByCarId(Long carId);
    int save(VehicleInfo vehicle);
}
