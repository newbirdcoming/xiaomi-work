package com.xiaomi.interfaces.controller;


import com.xiaomi.application.service.SignalApplicationService;
import com.xiaomi.application.service.VehicleApplicationService;
import com.xiaomi.domain.dto.vehicle.VehicleCreateRequest;
import com.xiaomi.domain.dto.vehicle.VehicleResponse;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.model.vehicle.VehicleInfo;
import com.xiaomi.domain.repository.VehicleRepository;
import com.xiaomi.interfaces.vo.ResponseResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleApplicationService vehicleApplicationService;


    @GetMapping("/getByCarId/{carId}")
    public ResponseResult getByCarId(@PathVariable("carId") Long carId) {
        Optional<VehicleResponse> byCarId = vehicleApplicationService.findByCarId(carId);
        if (byCarId.isPresent()) {
            return ResponseResult.success(byCarId.get());
        } else {
            return ResponseResult.error("500", "车辆不存在");
        }
    }


    @PostMapping("/saveVehicleInfo")
    public ResponseResult saveVehicleInfo(@RequestBody  VehicleCreateRequest vehicleInfo) {

        int save = vehicleApplicationService.createVehicle(vehicleInfo);
        if (save > 0) {
            return  ResponseResult.success();
        }
        else
            return ResponseResult.error("500", "保存失败");
    }




}
