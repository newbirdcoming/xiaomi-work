package com.xiaomi.application.service;

import com.xiaomi.domain.dto.warning.WarningRequest;
import com.xiaomi.domain.model.warning.WarningRecord;
import com.xiaomi.domain.model.warning.WarningResult;
import com.xiaomi.domain.repository.WarningRecordRepository;
import com.xiaomi.domain.repository.WarningRuleRepository;
import com.xiaomi.domain.service.WarningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class WarningRecordApplicationService {
    private final WarningRecordRepository warningRecordRepository;
    private final WarningRuleRepository warningRuleRepository;


    public List<WarningResult> processWarnings(List<WarningRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return new ArrayList<>(); // Return an empty list if requests are null or empty
        }

        List<WarningRecord> results = new ArrayList<>();
        for (WarningRequest request : requests) {
            try {
                List<WarningRecord> res = warningRecordRepository.processWarnings(request);
                if (res != null) {
                    results.addAll(res);
                }
            } catch (Exception e) {
                // Log the exception and continue processing other requests
                log.error("Error processing warnings for request: {}", request, e);
            }
        }

        return results.stream()
                .map(record -> {
                    try {
                        String batteryType = warningRuleRepository.getBatteryTypeByCarId(record.getCarId());
                        return new WarningResult(
                                record.getCarId(),
                                batteryType,
                                record.getWarnName(),
                                record.getWarnLevel()
                        );
                    } catch (Exception e) {
                        // Log the exception and return a partial result
                        log.error("Error fetching battery type for carId: {}", record.getCarId(), e);
                        return new WarningResult(
                                record.getCarId(),
                                null, // Set batteryType to null if an error occurs
                                record.getWarnName(),
                                record.getWarnLevel()
                        );
                    }
                })
                .collect(Collectors.toList());
    }
}
