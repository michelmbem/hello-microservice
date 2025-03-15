package org.addy.orderservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaTopicConfig {

    @Value("${kafka.topic.notifications}")
    private String notificationsTopic;

    @Bean
    public NewTopic createNotificationsTopic() {
        return new NewTopic(notificationsTopic, 1, (short) 1);
    }
}
