package com.xiaomi.infrastructure.persistence.repository;

import com.xiaomi.domain.model.warning.WarningRecord;
import com.xiaomi.domain.repository.WarningRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarningRecordRepositoryImpl implements WarningRecordRepository {
    @Override
    public void save(WarningRecord record) {

    }

    @Override
    public List<WarningRecord> findByCarId(String carId) {
        return null;
    }

    @Override
    public List<WarningRecord> findByCarIdOrderByCreateTimeDesc(Long carId) {
        return null;
    }

}
