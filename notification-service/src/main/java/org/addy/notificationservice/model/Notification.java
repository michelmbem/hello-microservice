package org.addy.notificationservice.model;

import java.util.Map;

public record Notification(
        String from,
        String to,
        String subject,
        String bodyTemplate,
        Map<String, Object> bodyModel
) {
}
