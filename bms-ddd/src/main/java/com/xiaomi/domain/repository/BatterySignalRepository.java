package com.xiaomi.domain.repository;

import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignal;
import com.xiaomi.domain.model.signal.BatterySignalDTO;

import java.util.List;
import java.util.Optional;


public interface BatterySignalRepository {
    List<BatterySignalDTO> findUnprocessedSignals(int limit);
    void markAsProcessed(Long signalId);


//    Optional<BatterySignalDTO> findLatestByCarId(String carId);

    int saveSign(BatterySignal batterySignal);


    int delete(Long id);

    int updateSign(BatterySignal batterySignal);

    List<BatterySignal> selectByCarId(Long carId);

    BatterySignal selectById(Long carId);
}
