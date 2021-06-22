package com.encircle360.oss.docsrabbit;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.docsrabbit.dto.thumbnail.ThumbnailRequestDTO;
import com.encircle360.oss.docsrabbit.dto.thumbnail.ThumbnailResultDTO;
import com.encircle360.oss.docsrabbit.service.ConverterService;
import com.encircle360.oss.docsrabbit.service.ThumbnailService;
import com.encircle360.oss.docsrabbit.util.IOUtils;

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

    @Test
    public void test_endpoint() throws Exception {
        ThumbnailRequestDTO thumbnailRequestDTO = ThumbnailRequestDTO.builder().build();
        post("/thumbnails", thumbnailRequestDTO, status().isBadRequest());

        thumbnailRequestDTO = ThumbnailRequestDTO
            .builder()
            .format("txt")
            .base64("dGVzdA==")
            .build();

        MvcResult result = post("/thumbnails", thumbnailRequestDTO, status().isOk());
        ThumbnailResultDTO resultDTO = resultToObject(result, ThumbnailResultDTO.class);

        Assertions.assertEquals(thumbnailService.getThumbnailExtension(), resultDTO.getFormat());
        Assertions.assertNotNull(resultDTO.getBase64());

        Files.write(Path.of("temp/api_test_thumbnail.png"), IOUtils.fromBase64(resultDTO.getBase64()));

        ClassPathResource testResource = new ClassPathResource("thumbnail/sunset.jpg");
        byte[] source = testResource.getInputStream().readAllBytes();

        thumbnailRequestDTO = ThumbnailRequestDTO
            .builder()
            .format("jpg")
            .base64(IOUtils.toBase64(source))
            .build();

        result = post("/thumbnails", thumbnailRequestDTO, status().isOk());
        resultDTO = resultToObject(result, ThumbnailResultDTO.class);

        Assertions.assertEquals(thumbnailService.getThumbnailExtension(), resultDTO.getFormat());
        Assertions.assertNotNull(resultDTO.getBase64());

        Files.write(Path.of("temp/api_test_sunset_thumbnail.png"), IOUtils.fromBase64(resultDTO.getBase64()));

    }
}
