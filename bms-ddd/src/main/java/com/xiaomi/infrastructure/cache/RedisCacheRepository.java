package com.xiaomi.infrastructure.cache;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.model.warning.WarningRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisCacheRepository implements CacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<BatterySignalDTO> getLatestSignal(String carId) {
        String key = "signal:latest:" + carId;
        try {
            String json = (String) redisTemplate.opsForValue().get(key);
            return Optional.ofNullable(objectMapper.readValue(json, BatterySignalDTO.class));
        } catch (Exception e) {
            log.error("Failed to get latest signal from cache", e);
            return Optional.empty();
        }
    }

    @Override
    public void cacheLatestSignal(BatterySignalDTO signal) {
        String key = "signal:latest:" + signal.getCarId();
        try {
            String json = objectMapper.writeValueAsString(signal);
            redisTemplate.opsForValue().set(key, json, 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Failed to cache latest signal", e);
        }
    }

    @Override
    public void evictLatestSignal(String carId) {

    }

    @Override
    public Optional<List<WarningRule>> getRulesByBatteryType(BatteryType batteryType) {
        return Optional.empty();
    }

    @Override
    public void cacheRules(BatteryType batteryType, List<WarningRule> rules) {

    }

    @Override
    public Optional<List<WarningRecord>> getWarningRecords(Long carId) {
        return Optional.empty();
    }

    @Override
    public void cacheWarningRecords(Long carId, List<WarningRecord> records) {

    }


    @Override
    public void evictWarningRecords(String carId) {

    }

    // 其他方法实现...
}
