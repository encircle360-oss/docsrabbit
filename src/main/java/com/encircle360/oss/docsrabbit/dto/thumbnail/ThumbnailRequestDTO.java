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
@Schema(name = "ThumbnailRequest", description = "A request for creating a thumbnail of the given document / image.")
public class ThumbnailRequestDTO {

    private String format;

    private String base64;

    private int width;

    private int height;

    private boolean container;
}
