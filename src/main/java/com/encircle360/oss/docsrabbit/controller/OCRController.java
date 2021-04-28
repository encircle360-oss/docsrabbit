package com.encircle360.oss.docsrabbit.controller;

import com.encircle360.oss.docsrabbit.dto.ocr.OCRResultDTO;
import com.encircle360.oss.docsrabbit.service.ocr.OCRService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class OCRController {

    private final OCRService ocrService;

    @Operation(
            operationId = "ocrParse",
            description = "Parses given multipart file and return OCRResultDTO containing scan result as text. Hint: Images embedded in e.g. PDF will be parse, too. "
    )
    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OCRResultDTO> ocrParse(@RequestParam("file") MultipartFile file) throws Exception {
        OCRResultDTO OCRResult = ocrService.parse(file.getInputStream());

        return ResponseEntity.ok(OCRResult);
    }
}
