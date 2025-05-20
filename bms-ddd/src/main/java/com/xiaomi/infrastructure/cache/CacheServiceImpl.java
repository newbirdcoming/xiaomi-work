package com.xiaomi.infrastructure.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.warning.WarningResult;
import com.xiaomi.domain.service.WarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {


    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;


    public CacheServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void evictLatestSignal(Long carId) {
        String key = "latestSignal::" + carId;
        redisTemplate.delete(key);
    }

    @Override
    @Cacheable(value = "latestSignal", key = "#carId")
    public Optional<BatterySignalDTO> getLatestSignal(String carId) {
        // 这个方法会被AOP拦截，实际不会执行到这里
        return Optional.empty();
    }

    @Override
    public void cacheWarningResults(String carId, List<WarningResult> results) {

    }

//    @Override
//    public void cacheWarningResults(String carId, List<WarningResult> results) {
//        String key = "warningResults::" + carId;
//        try {
//            String json = objectMapper.writeValueAsString(results);
//            redisTemplate.opsForValue().set(key, json, 1, TimeUnit.HOURS);
//        } catch (JsonProcessingException e) {
//            log.error("Failed to cache warning results", e);
//        }
//    }

    @Override
    public Optional<List<WarningResult>> getWarningResults(String carId) {
        return Optional.empty();
    }

    @Override
    public void cacheRules(String batteryType, List<WarningRule> rules) {

    }

    @Override
    public Optional<List<WarningRule>> getRules(String batteryType) {
        return Optional.empty();
    }
}