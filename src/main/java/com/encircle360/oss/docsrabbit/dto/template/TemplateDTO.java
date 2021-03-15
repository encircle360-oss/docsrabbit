package com.encircle360.oss.docsrabbit.dto.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Template")
public class TemplateDTO {

    @Schema(name = "id", description = "Id of the template in database")
    private String id;

    @Schema(name = "name", description = "Name of the template in database")
    private String name;

    @Schema(name = "content", description = "Content of the template in database (HTML, plain text, base64 encoded file content, etc.)")
    private String content;

    @Schema(name = "locale", description = "Locale of the template in database")
    private String locale;

    @Schema(name = "tags", description = "List of tags which this template has.")
    private List<String> tags;

    @Schema(name = "lastUpdate", description = "Added lastupdate for caching of templates")
    private LocalDateTime lastUpdate;
}
