package com.encircle360.oss.docsrabbit.service.template;

import com.encircle360.oss.docsrabbit.model.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;

@Slf4j
public abstract class AbstractTemplateLoader implements DocsRabbitTemplateLoader {

    protected String getFileContent(String path) {
        Resource resource = new ClassPathResource("templates/" + path);
        if (resource.exists()) {
            try {
                return new String(resource.getInputStream().readAllBytes());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        Path filePath = Paths.get("/resources/templates/" + path);
        if (Files.exists(filePath)) {
            try {
                return Files.readString(filePath);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return null;
    }

    protected Template loadFromFiles(String templateId) {
        if (!templateId.endsWith(".ftlh") && !templateId.endsWith(".ftl")) {
            templateId = templateId + ".ftlh";
        }

        String baseTemplateContent = getFileContent(templateId);

        if (baseTemplateContent == null) {
            return null;
        }

        return Template
                .builder()
                .id(templateId)
                .name(templateId)
                .html(baseTemplateContent)
                .build();
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        return loadTemplate(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        if (templateSource instanceof Template && ((Template) templateSource).getLastUpdate() != null) {
            return ((Template) templateSource).getLastUpdate().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        }
        return 0;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        String content = ((Template) templateSource).getHtml();

        return new StringReader(content);
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {

    }
}
