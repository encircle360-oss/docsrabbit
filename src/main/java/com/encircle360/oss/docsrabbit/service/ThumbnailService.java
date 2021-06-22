package com.encircle360.oss.docsrabbit.service;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.encircle360.oss.docsrabbit.util.IOUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final static String THUMBNAIL_EXTENSION = "png";

    public String getThumbnailExtension() {
        return THUMBNAIL_EXTENSION;
    }

    public String createBase64Thumbnail(
        @NonNull final String base64, @NonNull final String inputFormat, @NonNull Integer containerWidth, @NonNull Integer containerHeight, boolean container)
        throws Exception {
        return createBase64Thumbnail(IOUtils.fromBase64(base64), inputFormat, containerWidth, containerHeight, container);
    }

    public String createBase64Thumbnail(
        @NonNull final byte[] inputBytes, @NonNull final String inputFormat, @NonNull Integer containerWidth, @NonNull Integer containerHeight, boolean container)
        throws Exception {
        byte[] image = createThumbnail(inputBytes, inputFormat, containerWidth, containerHeight, container);
        return IOUtils.toBase64(image);
    }

    /**
     *
     */
    public byte[] createThumbnail(
        @NonNull final byte[] imageOfInput, @NonNull final String inputFormat, @NonNull Integer containerWidth, @NonNull Integer containerHeight, boolean container)
        throws Exception {
        if (!isImageFormat(inputFormat)) {
            throw new IllegalArgumentException("Input format is not an image");
        }

        InputStream imageOfInputStream = new ByteArrayInputStream(imageOfInput);
        BufferedImage inputImage = ImageIO.read(imageOfInputStream);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage thumbnailImage = Thumbnailator.createThumbnail(inputImage, containerWidth, containerHeight);

        if (container) {
            thumbnailImage = containerize(thumbnailImage, containerWidth, containerHeight);
        }

        // write image to byte array stream
        ImageIO.write(thumbnailImage, THUMBNAIL_EXTENSION, baos);

        return baos.toByteArray();
    }

    private BufferedImage containerize(BufferedImage image, int containerWidth, int containterHeight) {
        if (image.getHeight() == containterHeight && image.getWidth() == containerWidth) {
            return image;
        }

        BufferedImage container = transparentImage(containerWidth, containterHeight);
        int xoffset = (containerWidth - image.getWidth()) / 2;
        int yoffset = (containterHeight - image.getHeight()) / 2;

        Graphics2D drawer = container.createGraphics();
        drawer.drawImage(image, null, xoffset, yoffset);
        drawer.dispose();

        return container;
    }

    private BufferedImage transparentImage(int width, int height) {
        BufferedImage container = new BufferedImage(width, height, TYPE_INT_ARGB);

        Graphics2D drawer = container.createGraphics();
        drawer.setBackground(new Color(0, 0, 0, 1));
        drawer.dispose();

        return container;
    }

    public boolean isImageFormat(String format) {
        return format != null && (format.equals("png") || format.equals("jpg") || format.equals("jpeg") || format.equals("gif") || format.equals("tiff") || format.equals("bmp"));
    }

}
