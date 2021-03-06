package com.encircle360.oss.docsrabbit.controller;

import javax.validation.Valid;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encircle360.oss.docsrabbit.dto.thumbnail.ThumbnailRequestDTO;
import com.encircle360.oss.docsrabbit.dto.thumbnail.ThumbnailResultDTO;
import com.encircle360.oss.docsrabbit.service.ConverterService;
import com.encircle360.oss.docsrabbit.service.ThumbnailService;
import com.encircle360.oss.docsrabbit.util.IOUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/thumbnails")
public class ThumbnailController {

    private final ThumbnailService thumbnailService;
    private final ConverterService converterService;

    @Operation(
        operationId = "createThumbnail",
        description = "Creates a thumbnail by base64 input and the given format from request",
        responses = {
            @ApiResponse(responseCode = "200", description = "Thumbnail was generated successfully."),
            @ApiResponse(responseCode = "400", description = "The request body was not correct."),
            @ApiResponse(responseCode = "412", description = "The given format is not convertable."),
            @ApiResponse(responseCode = "500", description = "Some server error has happened, see logs for further details.")
        }
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThumbnailResultDTO> create(@RequestBody @Valid final ThumbnailRequestDTO request) {

        // check if correct format is submitted
        if (!converterService.isSupported(request.getFormat()) || (converterService.isIncompatible(request.getFormat(), thumbnailService
            .getThumbnailExtension()) && converterService.isIncompatible(request.getFormat(), "pdf"))) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        String result;
        try {
            byte[] pngImage = IOUtils.fromBase64(request.getBase64());
            String imageFormat = !thumbnailService.isImageFormat(request.getFormat()) ? "png" : request.getFormat();

            if (!thumbnailService.isImageFormat(request.getFormat())) {
                pngImage = converterService.convertToPng(pngImage, request.getFormat());
            }

            result = thumbnailService.createBase64Thumbnail(pngImage, imageFormat, request.getWidth(), request.getHeight(), request.isContainer());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ThumbnailResultDTO resultDTO = ThumbnailResultDTO
            .builder()
            .base64(result)
            .format(thumbnailService.getThumbnailExtension())
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(resultDTO);
    }
}
