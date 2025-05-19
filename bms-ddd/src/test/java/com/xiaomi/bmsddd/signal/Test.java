package com.xiaomi.bmsddd.signal;


import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.repository.BatterySignalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class Test {
    @Autowired
    BatterySignalRepository batterySignalRepository;

    @org.junit.jupiter.api.Test
    void test(){
        log.info("test");
        List<BatterySignalDTO> unprocessedSignals = batterySignalRepository.findUnprocessedSignals(100);
        for (BatterySignalDTO unprocessedSignal : unprocessedSignals) {
            System.out.println(unprocessedSignal);
        }
    }
}
