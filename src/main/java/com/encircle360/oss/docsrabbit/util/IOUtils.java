package com.encircle360.oss.docsrabbit.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IOUtils {

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
}
