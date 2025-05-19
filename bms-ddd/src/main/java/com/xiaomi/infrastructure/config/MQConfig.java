package com.xiaomi.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    public String getNameServer() {
        return nameServer;
    }

    public String getProducerGroup() {
        return producerGroup;
    }
    public static final String TOPIC = "battery-signal-topic";
    public static final String TAG = "warning";
}