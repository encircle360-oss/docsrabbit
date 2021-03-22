package com.encircle360.oss.docsrabbit.mapper;

import com.encircle360.oss.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.docsrabbit.dto.template.TemplateDTO;
import com.encircle360.oss.docsrabbit.model.Template;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TemplateMapper {

    TemplateMapper INSTANCE = Mappers.getMapper(TemplateMapper.class);

    TemplateDTO toDto(Template template);

    List<TemplateDTO> toDtos(List<Template> template);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdate", expression = "java(java.time.LocalDateTime.now())")
    Template createFromDto(CreateUpdateTemplateDTO createUpdateTemplateDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdate", expression = "java(java.time.LocalDateTime.now())")
    void updateFromDto(CreateUpdateTemplateDTO createUpdateTemplateDTO, @MappingTarget Template template);
}
