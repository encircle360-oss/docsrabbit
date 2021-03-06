package com.encircle360.oss.docsrabbit.controller;

import com.encircle360.oss.docsrabbit.config.MongoDbConfig;
import com.encircle360.oss.docsrabbit.dto.PageContainer;
import com.encircle360.oss.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.docsrabbit.dto.template.TemplateDTO;
import com.encircle360.oss.docsrabbit.mapper.TemplateMapper;
import com.encircle360.oss.docsrabbit.model.Template;
import com.encircle360.oss.docsrabbit.service.template.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Profile(MongoDbConfig.PROFILE)
@RequestMapping("/templates")
public class TemplateController {
    private final TemplateService templateService;

    private final TemplateMapper templateMapper = TemplateMapper.INSTANCE;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "listTemplates", description = "Returns a pageable list of templates")
    public ResponseEntity<PageContainer<TemplateDTO>> list(@RequestParam(required = false) String sort,
                                                           @RequestParam(required = false, defaultValue = "10") Integer size,
                                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                                           @RequestParam(required = false) List<String> tag) {
        Pageable pageable = PageRequest.of(page, size, sort == null ? Sort.unsorted() : Sort.by(sort));
        Page<Template> templatePage = templateService.findAll(tag, pageable);
        List<TemplateDTO> dtos = templateMapper.toDtos(templatePage.getContent());

        PageContainer<TemplateDTO> pageContainer = PageContainer.of(dtos, templatePage);
        return ResponseEntity.status(HttpStatus.OK).body(pageContainer);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "getTemplate", description = "Returns a templates by its id, if not found 404")
    public ResponseEntity<TemplateDTO> get(@PathVariable final String id) {
        Template template = templateService.get(id);
        if (template == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        TemplateDTO templateDTO = templateMapper.toDto(template);
        return ResponseEntity.status(HttpStatus.OK).body(templateDTO);
    }

    @Operation(operationId = "createTemplate", description = "Creates a template in database with the given contents")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateDTO> create(@RequestBody @Valid final CreateUpdateTemplateDTO createUpdateTemplateDTO) {
        Template template = templateMapper.createFromDto(createUpdateTemplateDTO);
        template = templateService.save(template);
        TemplateDTO templateDTO = templateMapper.toDto(template);

        return ResponseEntity.status(HttpStatus.CREATED).body(templateDTO);
    }

    @Operation(operationId = "updateTemplate", description = "Updates a template in database with the given contents")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateDTO> update(@PathVariable final String id, @RequestBody @Valid final CreateUpdateTemplateDTO createUpdateTemplateDTO) {
        Template template = templateService.get(id);
        if (template == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        templateMapper.updateFromDto(createUpdateTemplateDTO, template);
        template = templateService.save(template);
        TemplateDTO templateDTO = templateMapper.toDto(template);

        return ResponseEntity.status(HttpStatus.OK).body(templateDTO);
    }

    @Operation(operationId = "deleteTemplate", description = "Deletes a template in database with the given id")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable final String id) {
        Template template = templateService.get(id);
        if (template == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        templateService.delete(template);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
