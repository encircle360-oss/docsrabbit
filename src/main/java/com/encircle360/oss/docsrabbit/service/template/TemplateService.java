package com.encircle360.oss.docsrabbit.service.template;

import com.encircle360.oss.docsrabbit.config.MongoDbConfig;
import com.encircle360.oss.docsrabbit.model.Template;
import com.encircle360.oss.docsrabbit.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Profile(MongoDbConfig.PROFILE)
public class TemplateService {

    private final TemplateRepository templateRepository;

    public Page<Template> findAll(Pageable pageable) {
        return templateRepository.findAll(pageable);
    }

    public Template save(Template template) {
        return templateRepository.save(template);
    }

    public Template get(String id) {
        return templateRepository.findById(id).orElse(null);
    }

    public void delete(Template template) {
        templateRepository.delete(template);
    }

    public Page<Template> findAll(List<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            return findAll(pageable);
        }
        return templateRepository.findAllByTagsContains(tags, pageable);
    }

    public Template getByName(String name) {
        return templateRepository.findFirstByName(name);
    }
}
