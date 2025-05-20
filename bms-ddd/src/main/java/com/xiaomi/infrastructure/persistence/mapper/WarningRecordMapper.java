package com.xiaomi.infrastructure.persistence.mapper;

import com.xiaomi.domain.model.warning.WarningRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WarningRecordMapper {
    int save(WarningRecord record);
}
