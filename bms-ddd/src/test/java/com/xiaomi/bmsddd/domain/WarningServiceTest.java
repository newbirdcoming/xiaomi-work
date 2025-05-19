package com.xiaomi.bmsddd.domain;

import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.vehicle.BatteryType;
import com.xiaomi.domain.model.vehicle.VehicleInfo;
import com.xiaomi.domain.repository.VehicleRepository;
import com.xiaomi.domain.repository.WarningRecordRepository;
import com.xiaomi.domain.repository.WarningRuleRepository;
import com.xiaomi.domain.service.WarningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarningServiceTest {
    @Mock
    private VehicleRepository vehicleRepo;
    @Mock
    private WarningRuleRepository ruleRepo;
    @Mock
    private WarningRecordRepository warningRepo;

    @InjectMocks
    private WarningService warningService;

    @Test
    void processSignal_shouldGenerateWarning_whenRuleMatched() {
        // Given
        long carId = 123;
        BatterySignalDTO signal = new BatterySignalDTO();
        signal.setCarId(carId);
        signal.setMx(12.0);
        signal.setMi(10.0); // 电压差2.0

        VehicleInfo vehicle = new VehicleInfo();
        vehicle.setBatteryType(BatteryType.LITHIUM_ION);

        WarningRule rule = new WarningRule();
        rule.setRuleId(1);
        rule.setRuleName("电压差报警");
        rule.setBatteryType(BatteryType.LITHIUM_ION);
        rule.setConditionExpression("1<=(Mx－Mi)<3,报警等级：2");

        when(vehicleRepo.findByCarId(carId)).thenReturn(Optional.of(vehicle));
        when(ruleRepo.findByBatteryType(any())).thenReturn(Arrays.asList(rule));

        // When
        List<WarningService.WarningResult> results = warningService.processSignal(signal);

        // Then
        assertThat(results.isEmpty());
        assertThat(results.get(0).getWarnLevel()).isEqualTo(2);
        verify(warningRepo).save(argThat(w ->
                w.getWarnLevel() == 2 &&
                        w.getCarId().equals(carId)
        ));
    }
}
