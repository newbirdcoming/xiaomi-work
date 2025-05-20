package com.xiaomi.infrastructure.persistence.repository;

import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignal;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.repository.BatterySignalRepository;
import com.xiaomi.infrastructure.persistence.mapper.BatterySignalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.xiaomi.domain.model.signal.BatterySignalDTO.fromDbRecord;
@Slf4j
@Repository
@RequiredArgsConstructor
public class BatterySignalRepositoryImpl implements BatterySignalRepository {
    private final BatterySignalMapper mapper;



    @Override
    public List<BatterySignalDTO> findUnprocessedSignals(int limit) {
        List<BatterySignal> batterySignals = mapper.selectBatch(limit, false);
        return   batterySignals.stream().map(entity -> {
            try {
                return fromDbRecord(entity);
            } catch (Exception e) {
                log.error("转换失败: {}", entity.getId(), e);
                return null;  // 或返回默认值
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void markAsProcessed(Long signalId) {
        mapper.updateSignStatue(signalId,true);
    }



    @Override
    public int saveSign(BatterySignal batterySignal) {
        int res=mapper.saveSign(batterySignal);
        return res;
    }

    @Override
    public int delete(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public int updateSign(BatterySignal batterySignal) {
        return mapper.updateSign(batterySignal);
    }

    @Override
    public List<BatterySignal> selectByCarId(Long carId) {
        return mapper.selectByCarId(carId);
    }

    @Override
    public BatterySignal selectById(Long id) {
        return mapper.selectById(id);
    }


}

