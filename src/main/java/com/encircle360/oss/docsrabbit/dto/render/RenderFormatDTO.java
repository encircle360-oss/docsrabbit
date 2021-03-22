package com.encircle360.oss.docsrabbit.dto.render;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RenderFormat", description = "Defines a format for rendering")
public enum RenderFormatDTO {
    TEXT("plain/text"),
    HTML("text/html"),
    PDF("application/pdf"),
    XLS("application/msexcel");

    public final String value;

    RenderFormatDTO(String mimeType) {
        this.value = mimeType;
    }
}
