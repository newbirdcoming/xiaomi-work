package com.xiaomi.domain.model.signal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.MapUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatterySignalDTO {
    private Long id;
    private Long carId;
    private Double mx; // 最高电压
    private Double mi; // 最低电压
    private Double ix; // 最高电流
    private Double ii; // 最低电流

    private Boolean processed;

    private LocalDateTime signalTime;


    // 从JSON解析信号数据
    public static BatterySignalDTO fromJson(Long carId, String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(jsonData, HashMap.class);

            Double mx = MapUtils.getDouble(jsonMap, "Mx");
            Double mi = MapUtils.getDouble(jsonMap, "Mi");
            Double ix = MapUtils.getDouble(jsonMap, "Ix");
            Double ii = MapUtils.getDouble(jsonMap, "Ii");

            return BatterySignalDTO.builder()
                    .carId(carId)
                    .mx(mx)
                    .mi(mi)
                    .ix(ix)
                    .ii(ii)
                    .build();
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON解析失败", e);
        }
    }

    public static BatterySignalDTO newfromJson(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(jsonData, HashMap.class);
            Long id = MapUtils.getLong(jsonMap, "id");
            Long carId = MapUtils.getLong(jsonMap, "carId");
            Double mx = MapUtils.getDouble(jsonMap, "Mx");
            Double mi = MapUtils.getDouble(jsonMap, "Mi");
            Double ix = MapUtils.getDouble(jsonMap, "Ix");
            Double ii = MapUtils.getDouble(jsonMap, "Ii");
            Boolean processed = MapUtils.getBoolean(jsonMap, "processed");
            LocalDateTime signalTime = MapUtils.getLong(jsonMap, "signalTime") != null ? LocalDateTime.ofEpochSecond(MapUtils.getLong(jsonMap, "signalTime"), 0, ZoneId.systemDefault().getRules().getOffset(Instant.now())) : null;
            return BatterySignalDTO.builder()
                    .id(id)
                    .carId(carId)
                    .mx(mx)
                    .mi(mi)
                    .ix(ix)
                    .ii(ii)
                    .processed(processed)
                    .signalTime(signalTime)
                    .build();
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON解析失败", e);
        }
    }



    // 从数据库记录创建对象 将数据库查询结果使用
    public static BatterySignalDTO fromDbRecord(BatterySignal batterySignal) {
        BatterySignalDTO signal = fromJson(batterySignal.getCarId(),batterySignal.getSignalData());
        signal.setId(batterySignal.getId());
        signal.setProcessed(batterySignal.getStatue());
        signal.setSignalTime((batterySignal.getSignalTime()));
        return signal;
    }

    // 将 java.util.Date 转换为 LocalDateTime
    private static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    // 转换为数据库存储的JSON格式
    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("Mx", mx);
            jsonMap.put("Mi", mi);
            jsonMap.put("Ix", ix);
            jsonMap.put("Ii", ii);
            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }
}