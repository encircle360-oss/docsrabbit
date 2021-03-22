package com.encircle360.oss.docsrabbit.repository;

import com.encircle360.oss.docsrabbit.config.MongoDbConfig;
import com.encircle360.oss.docsrabbit.model.Template;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile(MongoDbConfig.PROFILE)
public interface TemplateRepository extends MongoRepository<Template, String> {
    Page<Template> findAllByTagsContains(List<String> tags, Pageable pageable);

    Template findFirstByName(String name);
}
