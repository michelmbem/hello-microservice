package org.addy.messaging;

import java.util.Map;

public record Notification(
        String from,
        String to,
        String subject,
        String bodyTemplate,
        Map<String, Object> bodyModel
) {
}
