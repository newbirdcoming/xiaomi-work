package com.xiaomi.domain.repository;

import com.xiaomi.domain.dto.warning.WarningRequest;
import com.xiaomi.domain.model.warning.WarningRecord;
import com.xiaomi.domain.service.WarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningRecordRepository {
    void save(WarningRecord record);
    

    List<WarningRecord> findByCarId(String carId);

    List<WarningRecord> findByCarIdOrderByCreateTimeDesc(Long carId);

    List<WarningRecord> processWarnings(WarningRequest request);
}