package com.xiaomi.interfaces.exception;



public class VehicleNotFoundException extends RuntimeException {
    private final Long carId;

    public VehicleNotFoundException(Long carId) {
        super("车辆不存在: " + carId);
        this.carId = carId;
    }

    public Long getCarId() {
        return carId;
    }
}
