package com.encircle360.oss.docsrabbit.dto.thumbnail;

import javax.validation.constraints.NotBlank;

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
    @Schema(name = "format", description = "The format of the input base64", example = "docx")
    private String format;

    @NotBlank
    @Schema(name = "base64", description = "Base64 encoded bytes of the file which should be made to a thumbnail")
    private String base64;

    @Builder.Default
    @Schema(name = "width", description = "The width of the generated thumbnail", defaultValue = "400")
    private int width = 400;

    @Builder.Default
    @Schema(name = "height", description = "The height of the generated thumbnail", defaultValue = "400")
    private int height = 400;

    @Builder.Default
    @Schema(name = "container", description = "Defines if the generated thumbnail will be in a container", defaultValue = "true")
    private boolean container = true;
}
