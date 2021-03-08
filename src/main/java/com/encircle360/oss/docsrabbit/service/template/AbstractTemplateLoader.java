package com.encircle360.oss.docsrabbit.service.template;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.encircle360.oss.docsrabbit.model.Template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplateLoader implements TemplateLoader {

    protected String getFileContent(String path) {
        Resource resource = new ClassPathResource("templates/" + path);
        if (resource.exists()) {
            try {
                return new String(resource.getInputStream().readAllBytes());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    protected Template loadFromFiles(String templateId) {
        String baseTemplateContent = getFileContent(templateId + ".ftlh");
        String plainTemplateContent = getFileContent(templateId + "_plain.ftlh");
        String subjectTemplateContent = getFileContent(templateId + "_subject.ftlh");

        if(baseTemplateContent == null && plainTemplateContent == null) {
            return null;
        }

        return Template
            .builder()
            .id(templateId)
            .name(templateId)
            .subject(subjectTemplateContent)
            .plain(plainTemplateContent)
            .html(baseTemplateContent)
            .build();
    }
}
