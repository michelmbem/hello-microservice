package org.addy.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.model.messaging.Notification;
import org.addy.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationListener {
    private final NotificationService notificationService;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void receiveNotification(Notification notification) {
        log.info("Received notification: {}, at: {}", notification, LocalDateTime.now());
        notificationService.send(notification);
    }
}
