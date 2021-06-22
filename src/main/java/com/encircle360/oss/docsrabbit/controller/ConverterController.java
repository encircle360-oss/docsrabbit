package com.encircle360.oss.docsrabbit.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encircle360.oss.docsrabbit.dto.convert.ConverterRequestDTO;
import com.encircle360.oss.docsrabbit.dto.convert.ConverterResultDTO;
import com.encircle360.oss.docsrabbit.service.ConverterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/convert")
public class ConverterController {

    private final ConverterService converterService;

    @Operation(
        operationId = "convert",
        description = "Converts a file from input type to output type of converterRequest",
        responses = {
            @ApiResponse(responseCode = "200", description = "Converting was successfull result is returned."),
            @ApiResponse(responseCode = "400", description = "The requestbody was not corret."),
            @ApiResponse(responseCode = "406", description = "The given input type is not supported."),
            @ApiResponse(responseCode = "412", description = "The input type can't be converted to the given output type."),
            @ApiResponse(responseCode = "500", description = "Some processing error has happened.")
        }
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConverterResultDTO> convert(@RequestBody @Valid final ConverterRequestDTO converterRequest) {

        if (!converterService.isSupported(converterRequest.getInputFormat())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        if (converterService.isIncompatible(converterRequest.getInputFormat(), converterRequest.getOutputFormat())) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        String base64;
        try {
            base64 = converterService.convertToBase64(converterRequest.getBase64(), converterRequest.getInputFormat(), converterRequest.getOutputFormat());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ConverterResultDTO result = ConverterResultDTO
            .builder()
            .format(converterRequest.getOutputFormat())
            .base64(base64)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
