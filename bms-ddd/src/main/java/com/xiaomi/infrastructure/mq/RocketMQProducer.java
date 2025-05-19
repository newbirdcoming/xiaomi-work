package com.xiaomi.infrastructure.mq;

import com.xiaomi.domain.model.signal.BatterySignalDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RocketMQProducer {
    private final RocketMQTemplate rocketMQTemplate;
    private DefaultMQProducer producer;

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Value("${rocketmq.producer.timeout:5000}")
    private int sendTimeout;

    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.setRetryTimesWhenSendAsyncFailed(0);
        producer.start();
    }

    public void sendSignalMessage(BatterySignalDTO signal) throws MQClientException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] sendSuccess = {false};

        try {
            Message msg = new Message("battery-signal-topic",
                    "warning",
                    signal.getId().toString(),
                    signal.toJson().getBytes(RemotingHelper.DEFAULT_CHARSET));

            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.debug("Message sent successfully: {}", sendResult.getMsgId());
                    sendSuccess[0] = true;
                    countDownLatch.countDown();
                }

                @Override
                public void onException(Throwable e) {
                    log.error("Failed to send message for signal ID: {}", signal.getId(), e);
                    countDownLatch.countDown();
                }
            });

            // Wait for send completion or timeout
            if (!countDownLatch.await(sendTimeout, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Send timeout for signal ID: " + signal.getId());
            }

            if (!sendSuccess[0]) {
                throw new RuntimeException("Failed to send message for signal ID: " + signal.getId());
            }
        } catch (Exception e) {
            log.error("Exception while sending message for signal ID: {}", signal.getId(), e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (producer != null) {
            producer.shutdown();
        }
    }
}