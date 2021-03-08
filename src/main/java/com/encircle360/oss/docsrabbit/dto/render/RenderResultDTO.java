package com.encircle360.oss.docsrabbit.dto.render;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RenderResult", description = "Holds the rendering results in base64 format and has additional information")
public class RenderResultDTO {

    @Schema(name = "templateId", description = "", example = "")
    private String templateId;

    @Schema(name = "mimeType", description = "", example = "application/pdf")
    private String mimeType;

    @Schema(name = "contentLength", description = "", example = "1024")
    private long contentLength;

    @Schema(name = "base64", description = "", example = "==0")
    private String base64;

}
