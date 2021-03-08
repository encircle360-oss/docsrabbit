package com.encircle360.oss.docsrabbit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.docsrabbit.dto.render.InlineRenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderResultDTO;

@Mapper
public interface RenderMapper {

    RenderMapper INSTANCE = Mappers.getMapper(RenderMapper.class);

    @Mapping(target = "mimeType", source = "renderRequestDTO.format.value")
    RenderResultDTO mapFromRequest(RenderRequestDTO renderRequestDTO, String base64, long contentLength);

    @Mapping(target = "templateId", ignore = true)
    @Mapping(target = "mimeType", source = "renderRequestDTO.format.value")
    RenderResultDTO mapFromInlineRequest(InlineRenderRequestDTO renderRequestDTO, String base64, long contentLength);
}
