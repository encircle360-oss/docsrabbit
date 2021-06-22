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

    private String base64;

    private String format;
}
