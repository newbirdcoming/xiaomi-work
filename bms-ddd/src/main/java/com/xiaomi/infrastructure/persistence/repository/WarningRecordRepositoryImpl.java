package com.xiaomi.infrastructure.persistence.repository;

import com.xiaomi.domain.dto.warning.WarningRequest;
import com.xiaomi.domain.model.warning.WarningRecord;
import com.xiaomi.domain.repository.WarningRecordRepository;
import com.xiaomi.domain.service.WarningService;
import com.xiaomi.infrastructure.persistence.mapper.WarningRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarningRecordRepositoryImpl implements WarningRecordRepository {
    @Autowired
    private WarningRecordMapper mapper;
    @Override
    public void save(WarningRecord record) {
        int res=mapper.save(record);
    }

    @Override
    public List<WarningRecord> findByCarId(String carId) {
        return null;
    }

    @Override
    public List<WarningRecord> findByCarIdOrderByCreateTimeDesc(Long carId) {
        return null;
    }

    @Override
    public List<WarningRecord> processWarnings(WarningRequest request) {
        return null;
    }

}
