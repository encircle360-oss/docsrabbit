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

import com.encircle360.oss.docsrabbit.dto.thumbnail.ThumbnailRequestDTO;
import com.encircle360.oss.docsrabbit.dto.thumbnail.ThumbnailResultDTO;
import com.encircle360.oss.docsrabbit.service.ConverterService;
import com.encircle360.oss.docsrabbit.service.ThumbnailService;
import com.encircle360.oss.docsrabbit.util.IOUtils;

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

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThumbnailResultDTO> create(@RequestBody @Valid final ThumbnailRequestDTO request) {

        // check if correct format is submitted
        if (!converterService.isSupported(request.getFormat()) || (converterService.isIncompatible(request.getFormat(), thumbnailService
            .getThumbnailExtension()) && converterService.isIncompatible(request.getFormat(), "pdf"))) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        String result;
        try {
            byte[] pngImage;
            if (thumbnailService.isImageFormat(request.getFormat())) {
                pngImage = IOUtils.fromBase64(request.getBase64());
            } else {
                pngImage = converterService.convertToPng(request.getBase64(), request.getFormat());
            }

            result = thumbnailService.createBase64Thumbnail(pngImage, request.getFormat(), request.getWidth(), request.getHeight(), request.isContainer());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ThumbnailResultDTO thumbnailResultDTO = ThumbnailResultDTO
            .builder()
            .base64(result)
            .format(thumbnailService.getThumbnailExtension())
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(thumbnailResultDTO);
    }
}
