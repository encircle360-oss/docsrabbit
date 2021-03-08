package com.encircle360.oss.docsrabbit.dto.render;

import java.util.HashMap;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RenderRequest", description = "Render Request ")
public class RenderRequestDTO {

    @NotNull
    @Schema(name = "format", description = "The format in which the template should be rendered")
    private RenderFormatDTO format;

    @NotBlank
    @Schema(name = "templateId", description = "Id of the template, that should be used for rendering")
    private String templateId;

    private HashMap<String, JsonNode> model;

}
