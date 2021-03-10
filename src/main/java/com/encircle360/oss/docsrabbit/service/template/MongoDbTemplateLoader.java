package com.encircle360.oss.docsrabbit.service.template;

import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.encircle360.oss.docsrabbit.config.MongoDbConfig;
import com.encircle360.oss.docsrabbit.model.Template;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Profile(MongoDbConfig.PROFILE)
public class MongoDbTemplateLoader extends AbstractTemplateLoader {

    private final TemplateService templateService;

    @Override
    public Template loadTemplate(String templateId) {
        if (templateId == null) {
            return null;
        }

        Template template = templateService.get(templateId);
        if (template == null) {
            return loadFromFiles(templateId);
        }
        return template;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        if (name.endsWith(".ftlh") || name.endsWith(".ftl")) {
            name = name.replace(".ftlh", "").replace(".ftl", "");
        }

        Template template = templateService.getByName(name);
        return template == null ? loadTemplate(name) : template;
    }

}
