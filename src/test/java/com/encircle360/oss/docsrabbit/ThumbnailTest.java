package com.encircle360.oss.docsrabbit;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.encircle360.oss.docsrabbit.service.ConverterService;
import com.encircle360.oss.docsrabbit.service.ThumbnailService;

@Disabled
@SpringBootTest
public class ThumbnailTest extends AbstractTest {

    @Autowired
    ThumbnailService thumbnailService;

    @Autowired
    ConverterService converterService;

    @Test
    public void test_thumbnail_creation_docx() throws Exception {
        ClassPathResource testResource = new ClassPathResource("convert/test.docx");
        byte[] source = testResource.getInputStream().readAllBytes();
        byte[] png = converterService.convertToPng(source, "docx");

        byte[] thumbnail = thumbnailService.createThumbnail(png, "png", 500, 500, true);
        Files.write(Path.of("temp/test_thumbail_container.png"), thumbnail);

        thumbnail = thumbnailService.createThumbnail(png, "png", 500, 500, false);
        Files.write(Path.of("temp/test_thumbail.png"), thumbnail);
    }

    @Test
    public void test_thumbnail_creation_png() throws Exception {
        ClassPathResource testResource = new ClassPathResource("thumbnail/logo.png");
        byte[] source = testResource.getInputStream().readAllBytes();

        byte[] thumbnail = thumbnailService.createThumbnail(source, "png", 500, 500, true);
        Files.write(Path.of("temp/test_logo_thumbail_container.png"), thumbnail);

        thumbnail = thumbnailService.createThumbnail(source, "png", 500, 500, false);
        Files.write(Path.of("temp/test_logo_thumbail.png"), thumbnail);
    }

    @Test
    public void test_thumbnail_creation_jpg() throws Exception {
        ClassPathResource testResource = new ClassPathResource("thumbnail/sunset.jpg");
        byte[] source = testResource.getInputStream().readAllBytes();

        byte[] thumbnail = thumbnailService.createThumbnail(source, "jpg", 500, 500, true);
        Files.write(Path.of("temp/test_sunset_thumbail_container.png"), thumbnail);

        thumbnail = thumbnailService.createThumbnail(source, "jpg", 500, 500, false);
        Files.write(Path.of("temp/test_sunset_thumbail.png"), thumbnail);
    }

    @Test
    public void test_thumbnail_different_scales_creation_jpg() throws Exception {
        ClassPathResource testResource = new ClassPathResource("thumbnail/sunset.jpg");
        byte[] source = testResource.getInputStream().readAllBytes();

        byte[] thumbnail = thumbnailService.createThumbnail(source, "jpg", 400, 500, true);
        Files.write(Path.of("temp/test_scale_400_sunset_thumbail.png"), thumbnail);

        thumbnail = thumbnailService.createThumbnail(source, "jpg", 500, 400, false);
        Files.write(Path.of("temp/test_scale_500_sunset_thumbail.png"), thumbnail);
    }
}
