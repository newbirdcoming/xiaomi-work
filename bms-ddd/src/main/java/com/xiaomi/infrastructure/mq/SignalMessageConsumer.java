package com.xiaomi.infrastructure.mq;

import com.xiaomi.domain.model.rule.WarningRule;
import com.xiaomi.domain.model.signal.BatterySignal;
import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.model.vehicle.VehicleInfo;
import com.xiaomi.domain.model.warning.WarningRecord;
import com.xiaomi.domain.repository.BatterySignalRepository;
import com.xiaomi.domain.repository.VehicleRepository;
import com.xiaomi.domain.repository.WarningRuleRepository;
import com.xiaomi.domain.service.WarningService;
import com.xiaomi.infrastructure.config.MQConfig;
import com.xiaomi.infrastructure.persistence.mapper.WarningRecordMapper;
import com.xiaomi.infrastructure.persistence.mapper.WarningRuleMapper;
import com.xiaomi.interfaces.exception.DMSException;
import com.xiaomi.interfaces.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class SignalMessageConsumer {
    private DefaultMQPushConsumer consumer;
    private final WarningService warningService;
    private final MQConfig mqConfig;
    private final WarningRecordMapper warningRecordMapper;
    private final BatterySignalRepository batterySignalRepository;
    private final WarningRuleRepository warningRuleRepository;

    private final VehicleRepository vehicleRepository;
    private final DeadLetterHandler deadLetterHandler;

    @Autowired
    public SignalMessageConsumer(WarningService warningService,
                                 MQConfig mqConfig,
                                 WarningRecordMapper warningRecordMapper,
                                 BatterySignalRepository batterySignalRepository,
                                 WarningRuleRepository warningRuleRepository,
                                 DeadLetterHandler deadLetterHandler,
                                 VehicleRepository vehicleRepository
                                 ) {
        this.warningService = warningService;
        this.mqConfig = mqConfig;
        this.warningRecordMapper = warningRecordMapper;
        this.batterySignalRepository = batterySignalRepository;
        this.warningRuleRepository = warningRuleRepository;
        this.deadLetterHandler = deadLetterHandler;
        this.vehicleRepository = vehicleRepository;
    }

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer(mqConfig.getProducerGroup()); // 建议使用独立consumer group
        consumer.setNamesrvAddr(mqConfig.getNameServer());
        consumer.subscribe(MQConfig.TOPIC, MQConfig.TAG);
        consumer.setMaxReconsumeTimes(0); // 禁用RocketMQ的重试机制

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    processSingleMessage(msg);
                } catch (Exception e) {
                    log.error("消息处理失败进入死信队列", e);
                    deadLetterHandler.handleFailedMessage(msg, e);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
        log.info("Consumer启动成功，Group={}, Topic={}, Tag={}",
                consumer.getConsumerGroup(), MQConfig.TOPIC, MQConfig.TAG);
    }

    private void processSingleMessage(MessageExt msg) {
        // 1. 解析消息
        String messageBody = new String(msg.getBody(), StandardCharsets.UTF_8);
        BatterySignalDTO signal = BatterySignalDTO.newfromJson(messageBody);
        log.debug("开始处理信号: signalId={}, carId={}", signal.getId(), signal.getCarId());

        // 2. 获取原始信号记录
        BatterySignal batterySignal = batterySignalRepository.selectById(signal.getId());
        if (batterySignal == null) {
            throw new IllegalArgumentException("信号记录不存在, ID: " + signal.getId());
        }
        Optional<VehicleInfo> byCarId = vehicleRepository.findByCarId(batterySignal.getCarId());
        if(!byCarId.isPresent()){
            throw new DMSException(ExceptionType.VEHICLE_NOT_FOUND.getErrorCode(),ExceptionType.VEHICLE_NOT_FOUND.SIGNAL_NOT_FOUND.getErrorMessage());
        }
        // 3. 获取该车辆的所有预警规则
        List<WarningRule> rules = warningRuleRepository.findByBatteryType(byCarId.get().getBatteryType().getDescription());
        if (rules.isEmpty()) {
            log.debug("未找到适用于carId={}的预警规则", signal.getCarId());
        }
        // 4. 规则匹配与预警生成
        rules.forEach(rule -> {
            try {
                // 4.1 解析规则条件（如果未解析）
                if (rule.getParsedConditions() == null) {
                    rule.parseConditions();
                }

                // 4.2 评估信号是否匹配规则
                Optional<Integer> warningLevel = rule.evaluate(signal);
                if (warningLevel.isPresent()) {
                    // 4.3 保存预警记录
                    WarningRecord record = WarningRecord.builder()
                            .carId(signal.getCarId())
                            .signalData(batterySignal.getSignalData())
                            .ruleId(rule.getRuleId())
                            .warnLevel(warningLevel.get())
                            .warnName(rule.getRuleName())
                            .build();
                    warningRecordMapper.save(record);
                    log.info("生成预警记录: ruleId={}, level={}", rule.getId(), warningLevel.get());
                }
            } catch (Exception e) {
                log.error("规则处理异常 ruleId={}", rule.getId(), e);
            }
        });

        // 5. 标记信号为已处理
        batterySignalRepository.markAsProcessed(signal.getId());
        log.info("信号处理完成: signalId={}", signal.getId());
    }

    @PreDestroy
    public void shutdown() {
        if (consumer != null) {
            consumer.shutdown();
            log.info("Consumer已关闭");
        }
    }
}