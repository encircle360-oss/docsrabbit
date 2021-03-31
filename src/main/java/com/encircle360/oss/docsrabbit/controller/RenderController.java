package com.encircle360.oss.docsrabbit.controller;

import com.encircle360.oss.docsrabbit.dto.render.InlineRenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.docsrabbit.mapper.RenderMapper;
import com.encircle360.oss.docsrabbit.model.Template;
import com.encircle360.oss.docsrabbit.service.FreemarkerService;
import com.encircle360.oss.docsrabbit.service.format.ExcelService;
import com.encircle360.oss.docsrabbit.service.format.PdfService;
import com.encircle360.oss.docsrabbit.service.template.DocsRabbitTemplateLoader;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/render")
public class RenderController {

    private final DocsRabbitTemplateLoader templateLoader;

    private final PdfService pdfService;
    private final ExcelService excelService;

    private final FreemarkerService freemarkerService;

    private final static Base64.Encoder base64Encoder = Base64.getEncoder();

    private final RenderMapper renderMapper = RenderMapper.INSTANCE;

    @Operation(operationId = "render", description = "Renders the given template by id")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenderResultDTO> render(@RequestBody @Valid RenderRequestDTO renderRequestDTO) throws Exception {
        Template contentTemplate = templateLoader.loadTemplate(renderRequestDTO.getTemplateId());
        if (contentTemplate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (renderRequestDTO.getLocale() != null) {
            contentTemplate.setLocale(renderRequestDTO.getLocale());
        }

        String processedTemplate = freemarkerService.parseTemplateFromString(contentTemplate.getContent(), contentTemplate.getLocale(), renderRequestDTO.getModel());
        String base64 = null;
        switch (renderRequestDTO.getFormat()) {
            case TEXT, HTML -> base64 = base64Encoder.encodeToString(processedTemplate.getBytes(StandardCharsets.UTF_8));
            case PDF -> {
                Template headerTemplate = templateLoader.loadTemplate(renderRequestDTO.getTemplateId() + "_header");
                Template footerTemplate = templateLoader.loadTemplate(renderRequestDTO.getTemplateId() + "_footer");
                String processedHeaderTemplate = null;
                String processedFooterTemplate = null;
                if (headerTemplate != null) {
                    processedHeaderTemplate = freemarkerService.parseTemplateFromString(headerTemplate.getContent(), contentTemplate.getLocale(), renderRequestDTO.getModel());
                }
                if (footerTemplate != null) {
                    processedFooterTemplate = freemarkerService.parseTemplateFromString(footerTemplate.getContent(), contentTemplate.getLocale(), renderRequestDTO.getModel());
                }
                base64 = pdfService.generateBase64PDFDocument(processedTemplate, processedHeaderTemplate, processedFooterTemplate);
            }
            case XLS -> base64 = excelService.generateBase64ExcelDocument(processedTemplate, renderRequestDTO.getTemplateId(), renderRequestDTO.getModel());
        }

        if (base64 == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        RenderResultDTO renderResultDTO = renderMapper.mapFromRequest(renderRequestDTO, base64, base64.getBytes().length);

        return ResponseEntity.status(HttpStatus.OK).body(renderResultDTO);
    }

    @Operation(operationId = "renderInline", description = "Renders the given inline template")
    @PostMapping(value = "/inline", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenderResultDTO> renderInline(@RequestBody @Valid InlineRenderRequestDTO inlineRenderRequestDTO) throws Exception {
        String processedTemplate = freemarkerService.parseTemplateFromString(inlineRenderRequestDTO.getTemplate(), inlineRenderRequestDTO.getLocale(), inlineRenderRequestDTO.getModel());

        String base64 = switch (inlineRenderRequestDTO.getFormat()) {
            case TEXT, HTML -> base64Encoder.encodeToString(processedTemplate.getBytes(StandardCharsets.UTF_8));
            case PDF -> pdfService.generateBase64PDFDocument(processedTemplate, null, null);
            default -> throw new UnsupportedOperationException("Inline rendering for " + inlineRenderRequestDTO.getFormat().name() + " is not supported.");

            // ToDo: Implement inline container XLSX file as base64 string
        };

        RenderResultDTO renderResult = renderMapper.mapFromInlineRequest(inlineRenderRequestDTO, base64, base64.getBytes().length);

        return ResponseEntity.status(HttpStatus.OK).body(renderResult);
    }

    @ExceptionHandler({TemplateException.class, IOException.class, InterruptedException.class})
    public ResponseEntity<String> handleException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
