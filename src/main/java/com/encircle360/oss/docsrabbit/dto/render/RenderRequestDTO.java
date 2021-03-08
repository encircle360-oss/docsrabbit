package com.encircle360.oss.docsrabbit.dto.render;

import javax.validation.constraints.NotBlank;

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
@Schema(name = "RenderRequest", description = "Render Request for a template in file system or database")
public class RenderRequestDTO extends AbstractRenderRequestDTO {

    @NotBlank
    @Schema(name = "templateId", description = "Id of the template, that should be used for rendering")
    private String templateId;

}
