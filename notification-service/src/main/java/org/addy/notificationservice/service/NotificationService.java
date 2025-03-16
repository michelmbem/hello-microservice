package org.addy.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.messaging.Notification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final JavaMailSender mailSender;
    private final TemplateLoader templateLoader;

    @Async
    public void send(Notification notification) {
        sendMail(notification.from(),
                notification.to(),
                notification.subject(),
                notification.bodyTemplate(),
                notification.bodyModel());
    }

    private void sendMail(String from, String to, String subject,
                          String template, Map<String, Object> model) {
        try {
            String body = templateLoader.loadTemplate(template, model);

            mailSender.send(mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setFrom(from);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body, true);
            });
        } catch (Exception e) {
            log.error("Could not send email to {}", to, e);
        }
    }
}
