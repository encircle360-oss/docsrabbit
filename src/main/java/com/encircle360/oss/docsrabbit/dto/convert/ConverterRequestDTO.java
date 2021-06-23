package com.encircle360.oss.docsrabbit.dto.convert;

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
@Schema(name = "ConverterRequest", description = "Request for converting one document of type inputFormat to type outputFormat.")
public class ConverterRequestDTO {

    @NotBlank
    @Schema(name = "inputFormat", description = "The input format, use file extension to describe", example = "pdf")
    private String inputFormat;

    @NotBlank
    @Schema(name = "outputFormat", description = "The output format, use file extension to describe", example = "png")
    private String outputFormat;

    @NotBlank
    @Schema(name = "base64", description = "The base64 encoded data of the input file")
    private String base64;
}
