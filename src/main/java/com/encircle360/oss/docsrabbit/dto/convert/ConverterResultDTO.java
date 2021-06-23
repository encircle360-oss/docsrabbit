package com.encircle360.oss.docsrabbit.dto.convert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ConverterResult", description = "Result of a document conversion")
public class ConverterResultDTO {

    @Schema(name = "format", description = "The file format of the base64", example = "pdf")
    private String format;

    @Schema(name = "base64", description = "Base64 encoded data of the converted file.")
    private String base64;
}
