package com.encircle360.oss.docsrabbit.service.template;

import com.encircle360.oss.docsrabbit.model.Template;
import freemarker.cache.TemplateLoader;

public interface DocsRabbitTemplateLoader extends TemplateLoader {

    Template loadTemplate(String templateId);
}
