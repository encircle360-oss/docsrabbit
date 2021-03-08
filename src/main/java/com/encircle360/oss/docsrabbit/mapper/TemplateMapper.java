package com.encircle360.oss.docsrabbit.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.docsrabbit.dto.template.TemplateDTO;
import com.encircle360.oss.docsrabbit.model.Template;

@Mapper
public interface TemplateMapper {

    TemplateMapper INSTANCE = Mappers.getMapper(TemplateMapper.class);

    TemplateDTO toDto(Template template);

    List<TemplateDTO> toDtos(List<Template> template);

    @Mapping(ignore = true, target = "id")
    Template createFromDto(CreateUpdateTemplateDTO createUpdateTemplateDTO);

    @Mapping(ignore = true, target = "id")
    void updateFromDto(CreateUpdateTemplateDTO createUpdateTemplateDTO, @MappingTarget Template template);
}
