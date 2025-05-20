package com.xiaomi.bmsddd.signal;


import com.xiaomi.domain.model.signal.BatterySignal;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.repository.BatterySignalRepository;
import com.xiaomi.infrastructure.mq.RocketMQProducer;
import com.xiaomi.infrastructure.mq.SignalMessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class Test {
    @Autowired
    BatterySignalRepository batterySignalRepository;

    @org.junit.jupiter.api.Test
    void test() {
        log.info("test");
        List<BatterySignalDTO> unprocessedSignals = batterySignalRepository.findUnprocessedSignals(100);
        for (BatterySignalDTO unprocessedSignal : unprocessedSignals) {
            System.out.println(unprocessedSignal);
        }
    }


    @org.junit.jupiter.api.Test
    void testFromJsonWithValidData() {
        String jsonData = "{\"Mx\":11.0,\"Mi\":9.6,\"Ix\":12.0,\"Ii\":11.7}";
        Long carId = 123L;

        BatterySignalDTO dto = BatterySignalDTO.fromJson(carId, jsonData);

        System.out.println("getIx" + dto.getIx());
        System.out.println("getIx" + dto.getMx());
        System.out.println("getIx" + dto.getIi());
        System.out.println("getIx" + dto.getMi());
    }

    @Autowired
    SignalMessageConsumer signalMessageConsumer;

    @Autowired
    RocketMQProducer rocketMQProducer;

    @org.junit.jupiter.api.Test
    void testMark() {
        batterySignalRepository.markAsProcessed(6L);
    }


    @org.junit.jupiter.api.Test
    void processSign() {
        BatterySignal batterySignal = batterySignalRepository.selectById(1L);

    }


}
