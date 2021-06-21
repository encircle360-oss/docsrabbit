package com.encircle360.oss.docsrabbit;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.encircle360.oss.docsrabbit.service.ConverterService;

@SpringBootTest
public class ConverterTest {

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
}
