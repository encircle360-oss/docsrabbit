package com.encircle360.oss.docsrabbit.dto.thumbnail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotBlank
    private String format;

    @NotBlank
    private String base64;

    @Builder.Default
    private int width = 400;

    @Builder.Default
    private int height = 400;

    @Builder.Default
    private boolean container = true;
}
