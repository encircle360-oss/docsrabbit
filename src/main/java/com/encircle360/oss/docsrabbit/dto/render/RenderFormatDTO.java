package com.encircle360.oss.docsrabbit.dto.render;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RenderFormat", description = "Defines a format for rendering")
public enum RenderFormatDTO {
    PDF("application/pdf"),TEXT("plain/text"), HTML("text/html");

    public final String value;

    private RenderFormatDTO(String mimeType) {
        this.value = mimeType;
    }

}
