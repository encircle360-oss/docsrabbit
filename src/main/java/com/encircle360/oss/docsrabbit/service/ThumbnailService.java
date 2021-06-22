package com.encircle360.oss.docsrabbit.service;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final static String THUMBNAIL_EXTENSION = "png";

    private final ConverterService converterService;

    /**
     * Creates a PNG thumbnail of the given source
     */
    public byte[] createThumbnail(
        @NonNull final byte[] inputBytes, @NonNull final String inputFormat, @NonNull Integer containerWidth, @NonNull Integer containerHeight, boolean container)
        throws Exception {
        if (!converterService.isSupported(inputFormat) || (converterService.isIncompatible(inputFormat, THUMBNAIL_EXTENSION)
            && converterService.isIncompatible(inputFormat, "pdf"))) {
            throw new IllegalArgumentException("format not supported");
        }

        String processFormat;
        byte[] processBytes;

        if (converterService.isIncompatible(inputFormat, THUMBNAIL_EXTENSION)) {
            processBytes = converterService.convert(inputBytes, inputFormat, "pdf");
            processFormat = "png";
        } else {
            processBytes = inputBytes;
            processFormat = inputFormat;
        }

        // write temp file of image
        byte[] imageOfInput = isImageFormat(inputFormat) ? inputBytes : converterService.convert(processBytes, processFormat, THUMBNAIL_EXTENSION);
        InputStream imageOfInputStream = new ByteArrayInputStream(imageOfInput);
        BufferedImage bufferedInputImage = ImageIO.read(imageOfInputStream);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage bufferedImage = Thumbnailator.createThumbnail(bufferedInputImage, containerWidth, containerHeight);

        if (container) {
            bufferedImage = rescale(bufferedImage, containerWidth, containerHeight);
        }

        // write image to byte array stream
        ImageIO.write(bufferedImage, THUMBNAIL_EXTENSION, baos);

        return baos.toByteArray();
    }

    private BufferedImage rescale(BufferedImage image, int containerWidth, int containterHeight) {
        if (image.getHeight() == containterHeight && image.getWidth() == containerWidth) {
            return image;
        }

        BufferedImage container = transparentImage(containerWidth, containterHeight);
        int xoffset = (containerWidth - image.getWidth()) / 2;
        int yoffset = (containterHeight - image.getHeight()) / 2;

        Graphics2D drawer = container.createGraphics();
        drawer.drawImage(image, null, xoffset, yoffset);
        drawer.setBackground(new Color(0, 0, 0, 1));
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

    private boolean isImageFormat(String format) {
        return format != null && (format.equals("png") || format.equals("jpg") || format.equals("jpeg") || format.equals("gif") || format.equals("tiff"));
    }

}
