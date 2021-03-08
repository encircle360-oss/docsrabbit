package com.encircle360.oss.docsrabbit.service.template;

import com.encircle360.oss.docsrabbit.model.Template;

public interface TemplateLoader {

    Template loadTemplate(String templateId);
}
