package com.xiaomi.infrastructure.cache;


import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.service.WarningService;

import java.util.List;
import java.util.Optional;

public interface CacheService {
    /**
     * 清除指定车辆的最新信号缓存
     * @param carId 车架编号
     */
    void evictLatestSignal(Long carId);

    /**
     * 获取缓存中的最新信号
     * @param carId 车架编号
     * @return 最新信号
     */
    Optional<BatterySignalDTO> getLatestSignal(String carId);

    /**
     * 缓存预警结果
     * @param carId 车架编号
     * @param results 预警结果列表
     */
    void cacheWarningResults(String carId, List<WarningService.WarningResult> results);

    /**
     * 从缓存获取预警结果
     * @param carId 车架编号
     * @return 预警结果列表
     */
    Optional<List<WarningService.WarningResult>> getWarningResults(String carId);

    /**
     * 缓存规则数据
     * @param batteryType 电池类型
     * @param rules 规则列表
     */
    void cacheRules(String batteryType, List<WarningRule> rules);

    /**
     * 从缓存获取规则数据
     * @param batteryType 电池类型
     * @return 规则列表
     */
    Optional<List<WarningRule>> getRules(String batteryType);



}
