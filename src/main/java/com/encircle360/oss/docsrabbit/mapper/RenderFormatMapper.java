package com.encircle360.oss.docsrabbit.mapper;

import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.docsrabbit.model.RenderFormat;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RenderFormatMapper {

    RenderFormatMapper INSTANCE = Mappers.getMapper(RenderFormatMapper.class);

    RenderFormatDTO toDto(RenderFormat renderFormat);

    RenderFormat fromDto(RenderFormatDTO renderFormatDTO);
}
