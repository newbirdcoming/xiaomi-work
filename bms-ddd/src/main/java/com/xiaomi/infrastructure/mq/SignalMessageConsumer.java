package com.xiaomi.infrastructure.mq;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.service.WarningService;
import com.xiaomi.infrastructure.config.MQConfig;
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
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class SignalMessageConsumer {
    private DefaultMQPushConsumer consumer;
    private final WarningService warningService;
    private final MQConfig mqConfig;

    @Autowired
    public SignalMessageConsumer(WarningService warningService, MQConfig mqConfig) {
        this.warningService = warningService;
        this.mqConfig = mqConfig;
    }

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer(mqConfig.getProducerGroup());
        consumer.setNamesrvAddr(mqConfig.getNameServer());
        consumer.subscribe(MQConfig.TOPIC, MQConfig.TAG);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    for (MessageExt msg : msgs) {
                        processMessage(msg);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    log.error("消息处理失败，稍后重试", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        consumer.start();
        log.info("Consumer启动成功，Group={}, Topic={}, Tag={}",
                consumer.getConsumerGroup(), MQConfig.TOPIC, MQConfig.TAG);
    }

    private void processMessage(MessageExt msg) {
        try {
            String messageBody = new String(msg.getBody(), StandardCharsets.UTF_8);
            log.info("收到消息: MsgId={}, Topic={}, Tags={}, Body={}",
                    msg.getMsgId(), msg.getTopic(), msg.getTags(), messageBody);

            BatterySignalDTO signal = BatterySignalDTO.fromJson(1L, messageBody);
            warningService.processSignal(signal);

            log.info("消息处理成功: MsgId={}", msg.getMsgId());
        } catch (Exception e) {
            log.error("消息处理异常: MsgId={}, Error={}", msg.getMsgId(), e.getMessage(), e);
            throw e;
        }
    }

    public void shutdown() {
        if (consumer != null) {
            consumer.shutdown();
            log.info("Consumer已关闭");
        }
    }
}