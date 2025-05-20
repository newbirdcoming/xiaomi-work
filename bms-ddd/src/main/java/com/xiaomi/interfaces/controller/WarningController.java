package com.xiaomi.interfaces.controller;

import com.xiaomi.application.service.SignalApplicationService;
import com.xiaomi.application.service.WarningRecordApplicationService;
import com.xiaomi.application.service.WarningRuleApplicationService;
import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.warning.WarningRecord;
import com.xiaomi.domain.model.warning.WarningResult;
import com.xiaomi.domain.service.WarningService;
import com.xiaomi.domain.dto.warning.WarningRequest;
import com.xiaomi.interfaces.exception.RuleNotFoundException;
import com.xiaomi.interfaces.vo.ResponseResult;
import com.xiaomi.interfaces.vo.WarningRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WarningController {
    private final WarningRecordApplicationService warningRecordApplicationService;
    private final WarningService warningService;


//    @PostMapping("/warning")
//    public ResponseResult<List<WarningService.WarningResult>> processWarnings(@RequestBody List<WarningRequest> requests) {
//        List<WarningService.WarningResult> results = new ArrayList<>();
//        for (WarningRequest request : requests) {
//            // 1. 上报信号
//            signalService.reportSignal(request.getCarId(), request.getSignal());
//
//            // 2. 处理信号
//            BatterySignalDTO signal = BatterySignalDTO.fromJson(request.getCarId(),request.getSignal());
//            if (request.getWarnId() != null) {
//                // 处理特定规则
//                WarningRule rule = warningService.getRuleById(request.getWarnId())
//                        .orElseThrow(() -> new RuleNotFoundException(request.getWarnId()));
//
//                rule.evaluate(signal).ifPresent(level -> {
//                    results.add(new WarningService.WarningResult(
//                            request.getCarId(),
//                            warningService.getVehicleBatteryType(request.getCarId()),
//                            rule.getRuleName(),
//                            level
//                    ));
//                });
//            } else {
//                // 处理所有规则
//                results.addAll(warningService.processSignal(signal));
//            }
//        }
//
//        return ResponseResult.success(results);
//    }

    @PostMapping("/warning")
    public ResponseResult<List<WarningResult>> processWarnings(@RequestBody List<WarningRequest> requests) {
        List<WarningResult> res=warningRecordApplicationService.processWarnings(requests);
        return ResponseResult.success(res);
    }

}