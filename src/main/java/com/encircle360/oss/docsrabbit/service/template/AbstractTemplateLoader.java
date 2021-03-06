package com.encircle360.oss.docsrabbit.service.template;

import com.encircle360.oss.docsrabbit.model.Template;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.ZoneOffset;

@Slf4j
public abstract class AbstractTemplateLoader implements DocsRabbitTemplateLoader {

    @Override
    public Object findTemplateSource(String name) {
        return this.loadTemplate(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        if (templateSource instanceof Template && ((Template) templateSource).getLastUpdate() != null) {
            return ((Template) templateSource).getLastUpdate().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        }
        return 0;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) {
        String content = ((Template) templateSource).getContent();

        return new StringReader(content);
    }

    @Override
    public void closeTemplateSource(Object templateSource) {

    }

    @Override
    public Resource getFileResource(String path) {
        Resource resource = new ClassPathResource("templates/" + path);
        if (!resource.exists()) {
            resource = new PathResource("/resources/templates/" + path);

            if (!resource.exists()) {
                return null;
            }
        }

        return resource;
    }

    protected Template loadFromFiles(@NonNull final String templateId) {
        String template = templateId;
        if (!template.endsWith(".ftlh") && !template.endsWith(".ftl")) {
            template = template + ".ftlh";
        }

        String baseTemplateContent = this.getFileContent(template);
        if (baseTemplateContent == null) {
            return null;
        }

        return Template
                .builder()
                .id(templateId)
                .name(templateId)
                .content(baseTemplateContent)
                .build();
    }

    protected String getFileContent(String path) {
        Resource resource = this.getFileResource(path);
        if (resource == null) {
            return null;
        }

        String fileContent = null;

        try {
            fileContent = new String(resource.getInputStream().readAllBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return fileContent;
    }
}
