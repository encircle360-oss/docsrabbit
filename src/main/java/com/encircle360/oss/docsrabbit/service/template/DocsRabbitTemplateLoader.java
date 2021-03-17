package com.encircle360.oss.docsrabbit.service.template;

import com.encircle360.oss.docsrabbit.model.Template;
import freemarker.cache.TemplateLoader;
import org.springframework.core.io.Resource;

public interface DocsRabbitTemplateLoader extends TemplateLoader {

    Template loadTemplate(String templateId);

    Resource getFileResource(String path);
}
