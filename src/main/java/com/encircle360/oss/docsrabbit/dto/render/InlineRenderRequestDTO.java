package com.encircle360.oss.docsrabbit.dto.render;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "InlineRenderRequest", description = "Request dto for rendering a template which is submitted inline")
public class InlineRenderRequestDTO extends AbstractRenderRequestDTO {

    @Schema(name = "template", description = "The contents of this template")
    private String template;

    @Schema(name = "locale", description = "Locale of this template")
    private String locale;
}
