package com.encircle360.oss.docsrabbit.dto.thumbnail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ThumbnailResult", description = "The result of creating a thumbnail")
public class ThumbnailResultDTO {

    @Schema(name = "base64", description = "The result of the thumbnail generation as base64 encoded string.")
    private String base64;

    @Schema(name = "format", description = "The file format of the base64 encoded bytes.")
    private String format;
}
