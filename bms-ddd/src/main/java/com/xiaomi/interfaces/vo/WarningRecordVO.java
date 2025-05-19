package com.xiaomi.interfaces.vo;

import com.xiaomi.domain.model.warning.WarningRecord;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 预警记录视图对象（API返回专用）
 */
@Data
public class WarningRecordVO {
    private Long id;
    private Long carId;
    private Integer ruleId;
    private String warnName;
    private Integer warnLevel;
    private String formattedCreateTime; // 格式化后的时间字符串

    /**
     * 从领域对象转换
     */
    public static WarningRecordVO fromDomain(WarningRecord record) {
        WarningRecordVO vo = new WarningRecordVO();
        vo.setId(record.getId());
        vo.setCarId(record.getCarId());
        vo.setRuleId(record.getRuleId());
        vo.setWarnName(record.getWarnName());
        vo.setWarnLevel(record.getWarnLevel());
        vo.setFormattedCreateTime(formatTime(record.getCreateTime()));
        return vo;
    }

    private static String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
