package com.xiaomi.infrastructure.mq;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import com.xiaomi.domain.repository.BatterySignalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@RequiredArgsConstructor
@Slf4j
public class RocketMQProducer {
    private final DeadLetterHandler deadLetterHandler;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Value("${rocketmq.name-server}")
    private String nameServerAddress;
    //
//    @Value("${rocketmq.producer.sendTimeout:3000}")
//    private int sendTimeout;
    @Autowired
    private final BatterySignalRepository batterySignalRepository;

    private DefaultMQProducer producer;
    private static final int MAX_RETRIES = 3; // 最大重试次数

    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServerAddress);
        producer.setSendMsgTimeout(3000);
        producer.start();
        log.info("RocketMQ Producer 初始化完成，Group: {}, NameServer: {}", producerGroup, nameServerAddress);
    }

    @PreDestroy
    public void shutdown() {
        if (producer != null) {
            producer.shutdown();
            log.info("RocketMQ Producer 已关闭");
        }
    }

    public void sendSignalMessage(BatterySignalDTO signal) {
        Message msg = buildMessage(signal);
        int retryCount = 0;
        boolean sendSuccess = false;

        while (retryCount <= MAX_RETRIES && !sendSuccess) {
            try {
                SendResult result = producer.send(msg);
                log.info("发送成功(尝试{}次) msgId={}", retryCount + 1, result.getMsgId());
                sendSuccess = true;
            } catch (Exception e) {
                retryCount++;
                if (retryCount > MAX_RETRIES) {
                    log.error("消息发送失败(已达最大重试次数{}次)", MAX_RETRIES);
                    handleFinalFailure(msg, e);
                } else {
                    log.warn("发送失败(第{}次重试)", retryCount);
                    sleepBackoff(retryCount);
                }
            }
        }
    }

    private Message buildMessage(BatterySignalDTO signal) {
        try {
            return new Message(
                    "SignalTopic", // 替换为实际的 Topic 名称
                    "warning",   // 替换为实际的 Tag 名称
                    signal.getId().toString(), // 使用 carId 作为消息键
                    signal.toString().getBytes(RemotingHelper.DEFAULT_CHARSET) // 消息体
            );
        } catch (Exception e) {
            log.error("构建消息失败", e);
            throw new RuntimeException("构建消息失败", e);
        }
    }

    private void handleFinalFailure(Message msg, Exception e) {
        try {

            String signIdKey = msg.getKeys();
            Long signId = Long.valueOf(signIdKey);

            batterySignalRepository.markAsProcessed(signId);

            deadLetterHandler.handleFailedMessage((MessageExt) msg, e);
        } catch (Exception ex) {
            log.error("处理失败消息时发生异常", ex);
            throw new RuntimeException("处理失败消息时发生异常", ex);
        }
    }

    private void sleepBackoff(int retryCount) {
        try {
            Thread.sleep(1000L * retryCount); // 线性退避
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}