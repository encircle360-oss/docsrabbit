package com.encircle360.oss.docsrabbit.controller;

import java.io.IOException;
import java.util.Base64;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.docsrabbit.mapper.RenderMapper;
import com.encircle360.oss.docsrabbit.model.Template;
import com.encircle360.oss.docsrabbit.service.FreemarkerService;
import com.encircle360.oss.docsrabbit.service.PdfService;
import com.encircle360.oss.docsrabbit.service.TemplateService;

import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/render")
public class RenderController {

    private final PdfService pdfService;

    private final TemplateService templateService;

    private final FreemarkerService freemarkerService;

    private final static Base64.Encoder base64Encoder = Base64.getEncoder();

    private final RenderMapper mapper = RenderMapper.INSTANCE;

    @Operation(operationId = "render", description = "Renders the given")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenderResultDTO> render(@RequestBody @Valid RenderRequestDTO renderRequestDTO) throws IOException, TemplateException, InterruptedException {
        Template template = templateService.get(renderRequestDTO.getTemplateId());

        if (template == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // todo catch errors and deliver codes
        String processed = freemarkerService.parseTemplateFromString(template.getHtml(), template.getLocale(), renderRequestDTO.getModel());
        byte[] pdfBytes = pdfService.generatePDFDocument(processed);
        String base64 = base64Encoder.encodeToString(pdfBytes);

        RenderResultDTO renderResultDTO = mapper.mapFromRequest(renderRequestDTO, base64, pdfBytes.length);
        return ResponseEntity.status(HttpStatus.OK).body(renderResultDTO);
    }

    @ExceptionHandler({TemplateException.class, IOException.class, InterruptedException.class})
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
