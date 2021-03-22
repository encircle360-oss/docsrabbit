package com.encircle360.oss.docsrabbit.dto.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateUpdateTemplate", description = "Data for creating a template")
public class CreateUpdateTemplateDTO {

    @NotBlank
    @Schema(name = "name", description = "Name of the template in database")
    private String name;

    @NotBlank
    @Schema(name = "content", description = "Content of the template in database (HTML, plain text, base64 encoded file content, etc.)")
    private String content;

    @Schema(name = "locale", description = "Locale of the template in database")
    private String locale;

    @Schema(name = "tags", description = "List of tags which this template has.")
    private List<String> tags;

}
