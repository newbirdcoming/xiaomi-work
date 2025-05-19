package com.xiaomi.infrastructure.mq;

import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DeadLetterHandler {
    private static final Logger dlqLogger = LoggerFactory.getLogger("deadLetterLogger");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final Path DLQ_DIR = Paths.get("logs/dlq");

    public void handleFailedMessage(MessageExt msg, Throwable cause) {
        String timestamp = LocalDateTime.now().format(formatter);
        String filename = "dlq_" + timestamp + "_" + msg.getMsgId() + ".log";

        try {
            // Ensure directory exists
            if (!DLQ_DIR.toFile().exists()) {
                DLQ_DIR.toFile().mkdirs();
            }

            Path filePath = DLQ_DIR.resolve(filename);
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write("Dead Letter Message:\n");
                writer.write("MsgId: " + msg.getMsgId() + "\n");
                writer.write("Topic: " + msg.getTopic() + "\n");
                writer.write("Tags: " + msg.getTags() + "\n");
                writer.write("Body: " + new String(msg.getBody()) + "\n");
                writer.write("Failure Cause: " + cause.getMessage() + "\n");
                writer.write("Stack Trace:\n");
                for (StackTraceElement element : cause.getStackTrace()) {
                    writer.write("\t" + element.toString() + "\n");
                }
            }

            dlqLogger.error("Message moved to dead letter queue: {}", msg.getMsgId(), cause);
        } catch (IOException e) {
            dlqLogger.error("Failed to write dead letter message to file: {}", msg.getMsgId(), e);
        }
    }
}