package com.encircle360.oss.docsrabbit.dto.render;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AbstractRenderRequest")
public abstract class AbstractRenderRequestDTO {
    @NotNull
    @Schema(name = "format", description = "The format in which the template should be rendered")
    private RenderFormatDTO format;

    @Schema(name = "model", description = "Map with all attributes needed for rendering the template")
    private HashMap<String, JsonNode> model;

    @Schema(name = "locale", description = "The locale which should be used for rendering, only needed when using filesystem", example = "de")
    private String locale;
}
