package com.xiaomi.application.service;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.repository.BatterySignalRepository;
import com.xiaomi.domain.service.WarningService;
import com.xiaomi.infrastructure.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SignalApplicationService {
    private final WarningService warningService;

//   预警查询
    @Transactional
    public void reportSignal(Long carId, String signalData) {
        // 1. 解析信号数据
        BatterySignalDTO signal = BatterySignalDTO.fromJson(carId,signalData);

//        // 2. 保存信号
//        signalRepo.saveSign(signal);

//        // 3. 清除缓存
//        cacheService.evictLatestSignal(carId);

        // 4. 实时处理信号并返回结果
        warningService.processSignal(signal);
    }


}
