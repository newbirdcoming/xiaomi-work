package com.xiaomi.domain.service;

import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.vehicle.VehicleInfo;
import com.xiaomi.domain.model.warning.WarningRecord;
import com.xiaomi.domain.model.warning.WarningResult;
import com.xiaomi.domain.repository.BatterySignalRepository;
import com.xiaomi.domain.repository.VehicleRepository;
import com.xiaomi.domain.repository.WarningRecordRepository;
import com.xiaomi.domain.repository.WarningRuleRepository;
import com.xiaomi.infrastructure.cache.CacheRepository;
import com.xiaomi.interfaces.exception.RuleNotFoundException;
import com.xiaomi.interfaces.exception.VehicleNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class WarningService {

    private final VehicleRepository vehicleRepo;

    private final WarningRuleRepository ruleRepo;
    private final BatterySignalRepository signalRepo;
    private final WarningRecordRepository warningRepo;

    private final CacheRepository cacheRepo;





    public List<WarningResult> processSignal(BatterySignalDTO signal) {
        // 1. 获取车辆信息
        VehicleInfo vehicle = vehicleRepo.findByCarId(signal.getCarId())
                .orElseThrow(() -> new VehicleNotFoundException(signal.getCarId()));

        // 2. 获取该电池类型的所有规则
        List<WarningRule> rules = ruleRepo.findByBatteryType(vehicle.getBatteryType().getDescription());

        // 3. 评估每条规则
        List<WarningResult> results = new ArrayList<>();
        for (WarningRule rule : rules) {
            rule.evaluate(signal).ifPresent(level -> {
                WarningRecord record = new WarningRecord();
                record.setCarId(signal.getCarId());
                record.setRuleId(rule.getRuleId());
                record.setWarnName(rule.getRuleName());
                record.setWarnLevel(level);
                record.setSignalData(signal.toJson());
                warningRepo.save(record);

                results.add(new WarningResult(
                        signal.getCarId(),
                        vehicle.getBatteryType().getDescription(),
                        rule.getRuleName(),
                        level
                ));
            });
        }

        return results;
    }

    public Optional<WarningRule> getRuleById(Integer ruleId) {
        // 先查缓存
        Optional<List<WarningRule>> cachedRules = cacheRepo.getRulesByBatteryType(null);
        if (cachedRules.isPresent()) {
            return cachedRules.get().stream()
                    .filter(rule -> rule.getRuleId().equals(ruleId))
                    .findFirst();
        }

        // 缓存未命中则查数据库
//        return ruleRepo.findByRuleId(ruleId);
        return Optional.empty();
    }

    public String getVehicleBatteryType(Long carId) {
        return vehicleRepo.findByCarId(carId)
                .map(vehicle -> vehicle.getBatteryType().getDescription())
                .orElseThrow(() -> new VehicleNotFoundException(carId));
    }

    // 新增特定规则处理方法
    public List<WarningResult> processSpecificRule(BatterySignalDTO signal, Integer ruleId) {
        VehicleInfo vehicle = vehicleRepo.findByCarId(signal.getCarId())
                .orElseThrow(() -> new VehicleNotFoundException(signal.getCarId()));

        WarningRule rule = getRuleById(ruleId)
                .filter(r -> r.getBatteryType() == vehicle.getBatteryType())
                .orElseThrow(() -> new RuleNotFoundException(ruleId));

        return rule.evaluate(signal)
                .map(level -> Arrays.asList(new WarningResult( // 改用Arrays.asList
                        signal.getCarId(),
                        vehicle.getBatteryType().getDescription(),
                        rule.getRuleName(),
                        level
                )))
                .orElse(Collections.emptyList());
    }



//
    public List<WarningRecord> getWarningsByCarId(Long carId) {
        // 1. 验证车辆是否存在
        if (!vehicleRepo.findByCarId(carId).isPresent()) {
            throw new VehicleNotFoundException(carId);
        }

        // 2. 优先从缓存获取
        Optional<List<WarningRecord>> cachedRecords = cacheRepo.getWarningRecords(carId);
        if (cachedRecords.isPresent()) {
            return cachedRecords.get();
        }

        // 3. 缓存未命中则查询数据库
        List<WarningRecord> records = warningRepo.findByCarIdOrderByCreateTimeDesc(carId);

        // 4. 写入缓存（异步避免阻塞）
        CompletableFuture.runAsync(() -> {
            cacheRepo.cacheWarningRecords(carId, records);
        });

        return records;
    }


}
