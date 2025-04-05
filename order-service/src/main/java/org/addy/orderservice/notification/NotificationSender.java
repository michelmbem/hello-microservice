package org.addy.orderservice.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.model.messaging.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationSender {

    @Value("${kafka.topic.notifications}")
    private String topicName;

    private final KafkaTemplate<String, Notification> kafkaTemplate;

    public void send(Notification notification) {
        log.info("Sending notification: {} to topic {}", notification, topicName);
        kafkaTemplate.send(topicName, notification);
    }
}
