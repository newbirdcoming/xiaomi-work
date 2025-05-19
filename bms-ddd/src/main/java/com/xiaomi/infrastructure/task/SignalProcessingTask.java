package com.xiaomi.infrastructure.task;

import com.xiaomi.infrastructure.mq.RocketMQProducer;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.repository.BatterySignalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignalProcessingTask {
    private final BatterySignalRepository signalRepo;
    private final RocketMQProducer producer;

    private static final int BATCH_SIZE = 100;

    @Scheduled(fixedRate = 5000) // 每5秒执行一次
    public void processUnprocessedSignals() {
        try {
            // 1. 查询未处理的信号(每次处理100条)
            List<BatterySignalDTO> signals = signalRepo.findUnprocessedSignals(BATCH_SIZE);

            if (signals.isEmpty()) {
                log.debug("未发现待处理信号");
                return;
            }

            log.info("发现 {} 条待处理信号", signals.size());

            // 2. 发送到MQ
            signals.forEach(signal -> {
                try {
                    // 发送消息到MQ
                    producer.sendSignalMessage(signal);

                    // 只有发送成功才标记为已处理
                    signalRepo.markAsProcessed(signal.getId());
                    log.debug("信号处理成功, ID: {}", signal.getId());
                } catch (Exception e) {
                    log.error("信号发送失败, ID: {}, 错误: {}", signal.getId(), e.getMessage());
                    // 失败不处理，等待下次扫描
                }
            });
        } catch (Exception e) {
            log.error("信号处理任务执行异常", e);
        }
    }
}