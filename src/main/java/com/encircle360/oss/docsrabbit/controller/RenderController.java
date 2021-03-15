package com.encircle360.oss.docsrabbit.controller;

import com.encircle360.oss.docsrabbit.dto.render.InlineRenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.docsrabbit.mapper.RenderMapper;
import com.encircle360.oss.docsrabbit.model.Template;
import com.encircle360.oss.docsrabbit.service.ExcelService;
import com.encircle360.oss.docsrabbit.service.FreemarkerService;
import com.encircle360.oss.docsrabbit.service.PdfService;
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

    private final RenderMapper mapper = RenderMapper.INSTANCE;

    @Operation(operationId = "render", description = "Renders the given template by id")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenderResultDTO> render(@RequestBody @Valid RenderRequestDTO renderRequestDTO) throws Exception {
        Template template = templateLoader.loadTemplate(renderRequestDTO.getTemplateId());
        if (template == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (renderRequestDTO.getLocale() != null) {
            template.setLocale(renderRequestDTO.getLocale());
        }

        String base64 = null;
        switch (renderRequestDTO.getFormat()) {
            case TEXT, HTML -> {
                String processedTemplate = freemarkerService.parseTemplateFromString(template.getHtml(), template.getLocale(), renderRequestDTO.getModel());
                base64 = base64Encoder.encodeToString(processedTemplate.getBytes(StandardCharsets.UTF_8));
            }
            case PDF -> {
                String processedTemplate = freemarkerService.parseTemplateFromString(template.getHtml(), template.getLocale(), renderRequestDTO.getModel());
                base64 = pdfService.generateBase64PDFDocument(processedTemplate);
            }
            case XLS -> {
                String processedTemplate = freemarkerService.parseTemplateFromString(template.getHtml(), template.getLocale(), renderRequestDTO.getModel());
                base64 = excelService.generateBase64ExcelDocument(processedTemplate);
            }
        }
        ;

        RenderResultDTO renderResultDTO = mapper.mapFromRequest(renderRequestDTO, base64, base64.getBytes().length);

        return ResponseEntity.status(HttpStatus.OK).body(renderResultDTO);
    }

    @Operation(operationId = "renderInline", description = "Renders the given inline template")
    @PostMapping(value = "/inline", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RenderResultDTO> renderInline(@RequestBody @Valid InlineRenderRequestDTO inlineRenderRequestDTO) throws Exception {
        String processed = freemarkerService.parseTemplateFromString(inlineRenderRequestDTO.getTemplate(), inlineRenderRequestDTO.getLocale(), inlineRenderRequestDTO.getModel());
        String processedPlain = freemarkerService.parseTemplateFromString(inlineRenderRequestDTO.getTemplate(), inlineRenderRequestDTO.getLocale(), inlineRenderRequestDTO.getModel());

        String base64 = switch (inlineRenderRequestDTO.getFormat()) {
            case TEXT -> base64Encoder.encodeToString(processedPlain.getBytes(StandardCharsets.UTF_8));
            case HTML -> base64Encoder.encodeToString(processed.getBytes(StandardCharsets.UTF_8));
            case PDF -> pdfService.generateBase64PDFDocument(processed);
            case XLS -> excelService.generateBase64ExcelDocument(processed);
        };

        RenderResultDTO renderResult = mapper.mapFromInlineRequest(inlineRenderRequestDTO, base64, base64.getBytes().length);

        return ResponseEntity.status(HttpStatus.OK).body(renderResult);
    }

    @ExceptionHandler({TemplateException.class, IOException.class, InterruptedException.class})
    public ResponseEntity<String> handleException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
