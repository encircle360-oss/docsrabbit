package com.encircle360.oss.docsrabbit.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IOUtils {

    private final static Base64.Decoder decoder = Base64.getDecoder();
    private final static Base64.Encoder encoder = Base64.getEncoder();

    private IOUtils() {}

    public static Path createRandomTmpFile(String fileExtension) throws IOException {
        return Files.createTempFile(Path.of("/tmp/"), null, "." + fileExtension);
    }

    public static void deleteWithoutThrow(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static String toBase64(byte[] bytes){
        return encoder.encodeToString(bytes);
    }

    public static byte[] fromBase64(String base64){
        return decoder.decode(base64);
    }
}
