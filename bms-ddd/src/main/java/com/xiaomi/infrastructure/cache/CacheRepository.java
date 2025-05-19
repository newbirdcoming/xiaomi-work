package com.xiaomi.infrastructure.cache;





import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.model.warning.WarningRecord;

import java.util.List;
import java.util.Optional;

public interface CacheRepository {
    // 信号缓存
    Optional<BatterySignalDTO> getLatestSignal(String carId);
    void cacheLatestSignal(BatterySignalDTO signal);
    void evictLatestSignal(String carId);

    // 规则缓存
    Optional<List<WarningRule>> getRulesByBatteryType(BatteryType batteryType);
    void cacheRules(BatteryType batteryType, List<WarningRule> rules);

    // 预警记录缓存
    Optional<List<WarningRecord>> getWarningRecords(Long carId);
    void cacheWarningRecords(Long carId, List<WarningRecord> records);
    void evictWarningRecords(String carId);
}
