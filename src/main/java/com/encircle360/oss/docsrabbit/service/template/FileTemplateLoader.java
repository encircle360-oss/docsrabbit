package com.encircle360.oss.docsrabbit.service.template;

import com.encircle360.oss.docsrabbit.config.MongoDbConfig;
import com.encircle360.oss.docsrabbit.model.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!" + MongoDbConfig.PROFILE)
public class FileTemplateLoader extends AbstractTemplateLoader {

    @Override
    public Template loadTemplate(String templateId) {
        return loadFromFiles(templateId);
    }
}
