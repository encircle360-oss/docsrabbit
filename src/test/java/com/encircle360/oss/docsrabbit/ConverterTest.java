package com.encircle360.oss.docsrabbit;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.docsrabbit.dto.convert.ConverterRequestDTO;
import com.encircle360.oss.docsrabbit.dto.convert.ConverterResultDTO;
import com.encircle360.oss.docsrabbit.service.ConverterService;

//@Disabled
@SpringBootTest
public class ConverterTest extends AbstractTest {

    @Autowired ConverterService converterService;

    @Test
    public void test_converting() throws Exception {
        ClassPathResource testResource = new ClassPathResource("convert/test.docx");
        byte[] source = testResource.getInputStream().readAllBytes();

        byte[] result = converterService.convert(source, "docx", "pdf");
        Path resultPdf = Path.of("temp/test_result.pdf");
        Files.write(resultPdf, result);

        byte[] pdfSource = Files.readAllBytes(resultPdf);
        result = converterService.convert(pdfSource, "pdf", "png");
        Path resultPng = Path.of("temp/test_result.png");
        Files.write(resultPng, result);

        Assertions.assertThrows(IllegalArgumentException.class, () -> converterService.convert(source, "docx", "asdf"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> converterService.convert(source, "docx", "csv"));
    }

    @Test
    public void test_endpoint() throws Exception {
        ConverterRequestDTO converterRequestDTO = ConverterRequestDTO.builder().build();
        post("/convert", converterRequestDTO, status().isBadRequest());

        converterRequestDTO = ConverterRequestDTO
            .builder()
            .inputFormat("not")
            .outputFormat("existent")
            .base64("empty")
            .build();
        post("/convert", converterRequestDTO, status().isNotAcceptable());

        converterRequestDTO = ConverterRequestDTO
            .builder()
            .inputFormat("docx")
            .outputFormat("png")
            .base64("empty")
            .build();
        post("/convert", converterRequestDTO, status().isPreconditionFailed());

        converterRequestDTO = ConverterRequestDTO
            .builder()
            .inputFormat("docx")
            .outputFormat("jpg")
            .base64("empty")
            .build();
        post("/convert", converterRequestDTO, status().isPreconditionFailed());

        converterRequestDTO = ConverterRequestDTO
            .builder()
            .inputFormat("docx")
            .outputFormat("pdf")
            .base64("empty")
            .build();
        post("/convert", converterRequestDTO, status().isInternalServerError());

        converterRequestDTO = ConverterRequestDTO
            .builder()
            .inputFormat("txt")
            .outputFormat("pdf")
            .base64("dGVzdA==")
            .build();

        MvcResult result = post("/convert", converterRequestDTO, status().isOk());
        ConverterResultDTO resultDTO = resultToObject(result, ConverterResultDTO.class);

        Assertions.assertEquals(converterRequestDTO.getOutputFormat(), resultDTO.getFormat());
        Assertions.assertNotNull(resultDTO.getBase64());

    }
}
