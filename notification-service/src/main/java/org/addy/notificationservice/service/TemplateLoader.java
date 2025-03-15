package org.addy.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class TemplateLoader {
    private final TemplateEngine templateEngine;

    public String loadTemplate(String template, Map<String, Object> model) {
        var context = new Context();
        context.setVariables(model);

        return templateEngine.process(template, context);
    }
}
