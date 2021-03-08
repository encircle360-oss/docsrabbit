package com.encircle360.oss.docsrabbit.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encircle360.oss.docsrabbit.dto.render.InlineRenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.docsrabbit.mapper.RenderMapper;
import com.encircle360.oss.docsrabbit.model.Template;
import com.encircle360.oss.docsrabbit.service.FreemarkerService;
import com.encircle360.oss.docsrabbit.service.PdfService;
import com.encircle360.oss.docsrabbit.service.template.TemplateLoader;

import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/render")
public class RenderController {

    private final PdfService pdfService;

    private final TemplateLoader templateLoader;

    private final FreemarkerService freemarkerService;

    private final static Base64.Encoder base64Encoder = Base64.getEncoder();

    private final RenderMapper mapper = RenderMapper.INSTANCE;

    @Operation(operationId = "render", description = "Renders the given template by id")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenderResultDTO> render(@RequestBody @Valid RenderRequestDTO renderRequestDTO) throws Exception {
        Template template = templateLoader.loadTemplate(renderRequestDTO.getTemplateId());

        if (template == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // todo catch errors and deliver codes
        String processed = freemarkerService.parseTemplateFromString(template.getHtml(), template.getLocale(), renderRequestDTO.getModel());
        String processedPlain = freemarkerService.parseTemplateFromString(template.getPlain(), template.getLocale(), renderRequestDTO.getModel());

        String base64 = switch (renderRequestDTO.getFormat()) {
            case PDF -> pdfService.generateBase64PDFDocument(processed);
            case HTML -> base64Encoder.encodeToString(processed.getBytes(StandardCharsets.UTF_8));
            case TEXT -> base64Encoder.encodeToString(processedPlain.getBytes(StandardCharsets.UTF_8));
        };

        RenderResultDTO renderResultDTO = mapper.mapFromRequest(renderRequestDTO, base64, base64.getBytes().length);
        return ResponseEntity.status(HttpStatus.OK).body(renderResultDTO);
    }

    @Operation(operationId = "renderInline", description = "Renders the given inline template")
    @PostMapping(value = "/inline", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenderResultDTO> renderInline(@RequestBody @Valid InlineRenderRequestDTO inlineRenderRequestDTO) throws Exception {
        String processed = freemarkerService.parseTemplateFromString(inlineRenderRequestDTO.getTemplate(), inlineRenderRequestDTO.getLocale(), inlineRenderRequestDTO.getModel());
        String processedPlain = freemarkerService.parseTemplateFromString(inlineRenderRequestDTO.getTemplate(), inlineRenderRequestDTO.getLocale(), inlineRenderRequestDTO.getModel());

        String base64 = switch (inlineRenderRequestDTO.getFormat()) {
            case PDF -> pdfService.generateBase64PDFDocument(processed);
            case HTML -> base64Encoder.encodeToString(processed.getBytes(StandardCharsets.UTF_8));
            case TEXT -> base64Encoder.encodeToString(processedPlain.getBytes(StandardCharsets.UTF_8));
        };

        RenderResultDTO renderResult = mapper.mapFromInlineRequest(inlineRenderRequestDTO, base64, base64.getBytes().length);
        return ResponseEntity.status(HttpStatus.OK).body(renderResult);
    }

    @ExceptionHandler({TemplateException.class, IOException.class, InterruptedException.class})
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
