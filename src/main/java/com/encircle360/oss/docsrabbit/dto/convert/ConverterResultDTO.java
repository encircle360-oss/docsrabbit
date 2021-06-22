package com.encircle360.oss.docsrabbit.dto.convert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConverterResultDTO {

    private String format;

    private String base64;
}
