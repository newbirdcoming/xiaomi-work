//package com.xiaomi.bmsddd.aplication;
//
//import com.xiaomi.application.service.SignalApplicationService;
//import com.xiaomi.domain.model.signal.BatterySignalDTO;
//import com.xiaomi.domain.repository.BatterySignalRepository;
//import com.xiaomi.domain.service.WarningService;
//import com.xiaomi.infrastructure.cache.CacheService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.argThat;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class SignalApplicationServiceTest {
//    @Mock
//    private BatterySignalRepository signalRepo;
//    @Mock
//    private CacheService cacheService;
//    @Mock
//    private WarningService warningService;
//
//    @InjectMocks
//    private SignalApplicationService signalService;
//
//    @Test
//    void reportSignal_shouldSaveSignalAndClearCache() {
//        // Given
//        long carId = 12L;
//        String signalData = "{\"Mx\":12.0,\"Mi\":11.8}";
//
//        BatterySignalDTO signal = BatterySignalDTO.fromJson(carId, signalData);
//
//        // When
//        signalService.reportSignal(carId, signalData);
//
//        // Then
//        verify(signalRepo).save(argThat(s ->
//                s.getCarId().equals(carId) &&
//                        s.getMx() == 12.0 &&
//                        s.getMi() == 11.8
//        ));
//        verify(cacheService).evictLatestSignal(carId);
//        verify(warningService).processSignal(any());
//    }
//}
//
