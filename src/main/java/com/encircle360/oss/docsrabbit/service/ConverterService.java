package com.encircle360.oss.docsrabbit.service;

import static com.encircle360.oss.docsrabbit.util.IOUtils.createRandomTmpFile;
import static com.encircle360.oss.docsrabbit.util.IOUtils.deleteWithoutThrow;
import static com.encircle360.oss.docsrabbit.util.IOUtils.fromBase64;
import static com.encircle360.oss.docsrabbit.util.IOUtils.toBase64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.encircle360.oss.docsrabbit.util.IOUtils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConverterService {

    private final static String PDF_EXTENSION = "pdf";
    private final static String PNG_EXTENSION = "png";

    private final List<String> TEXT_OUTPUT = List.of(
        "bib", "xml", "html", "ltx", "doc", "odt", "txt", "pdf", "rtf", "sdw"
    );

    private final List<String> GRAPHIC_OUTPUT = List.of(
        "eps", "emf", "gif", "jpg", "odd", "pdf", "png", "svg", "tiff", "bmp"
    );

    private final List<String> TABLE_OUTPUT = List.of(
        "csv", "html", "xls", "xml", "ods", "pdf", "sdc", "xhtml"
    );

    private final Map<String, List<String>> ACCEPTABLE_INPUT_OUTPUT = new HashMap<>();

    public ConverterService() {
        ACCEPTABLE_INPUT_OUTPUT.put("xml", TEXT_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("html", TEXT_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("doc", TEXT_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("docx", TEXT_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("odt", TEXT_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("txt", TEXT_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("rtf", TEXT_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("sdw", TEXT_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("eps", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("emf", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("gif", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("jpg", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("odd", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("png", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("tiff", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("bmp", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("pdf", GRAPHIC_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("csv", TABLE_OUTPUT);
        ACCEPTABLE_INPUT_OUTPUT.put("xls", TABLE_OUTPUT);
    }

    public String convertToBase64(String base64, String inputFormat, String outputFormat) throws Exception {
        byte[] converted = convert(fromBase64(base64), inputFormat, outputFormat);
        return toBase64(converted);
    }

    public String convertToBase64(byte[] inputBytes, String inputFormat, String outputFormat) throws Exception {
        byte[] converted = convert(inputBytes, inputFormat, outputFormat);
        return toBase64(converted);
    }

    public byte[] convert(String base64, String inputFormat, String outputFormat) throws Exception {
        return convert(fromBase64(base64), inputFormat, outputFormat);
    }

    public byte[] convertToPng(String base64, String inputFormat) throws Exception {
        return convertToPng(IOUtils.fromBase64(base64), inputFormat);
    }

    public byte[] convertToPng(byte[] bytes, String inputFormat) throws Exception {
        String processFormat;
        byte[] processBytes;

        if (isIncompatible(inputFormat, PDF_EXTENSION) && isIncompatible(inputFormat, PNG_EXTENSION)) {
            throw new IllegalArgumentException("Input format is not compatible");
        }

        if (isIncompatible(inputFormat, PNG_EXTENSION)) {
            processBytes = convert(bytes, inputFormat, PDF_EXTENSION);
            processFormat = PDF_EXTENSION;
        } else {
            processBytes = bytes;
            processFormat = inputFormat;
        }

        // write temp file of image
        return convert(processBytes, processFormat, PNG_EXTENSION);
    }

    /**
     * Converts input type to output type,
     * if the conversion doesnt work
     */
    public byte[] convert(@NonNull byte[] inputBytes, @NonNull String inputFormat, @NonNull String outputFormat) throws Exception {
        if (inputBytes == null || inputFormat == null || outputFormat == null) {
            throw new IllegalArgumentException("None of the arguments should be null");
        }

        if (isIncompatible(inputFormat, outputFormat)) {
            throw new IllegalArgumentException("Types are not compatible to convert");
        }

        Path outputFile = createRandomTmpFile(outputFormat);
        Path inputFile = createRandomTmpFile(inputFormat);

        Files.write(inputFile, inputBytes);

        Runtime runtime = Runtime.getRuntime();
        String processDefinition = "unoconv -f " + outputFormat + " -o " + outputFile.toString() + " " + inputFile.toString();
        Process process = runtime.exec(processDefinition);

        // read process results, to see if everything is executed
        String line;
        StringBuilder result = new StringBuilder();
        InputStreamReader streamReader = new InputStreamReader(process.getInputStream());
        BufferedReader input = new BufferedReader(streamReader);

        while ((line = input.readLine()) != null) {
            result.append(line);
        }

        String logResult = result.toString();
        if (logResult.contains("UnoException") || logResult.contains("is not known to unoconv")) {
            throw new IllegalArgumentException("Unoconv error happened: " + logResult);
        }

        if (!logResult.isBlank()) {
            log.info(logResult);
        }

        byte[] resultBytes = Files.readAllBytes(outputFile);

        deleteWithoutThrow(inputFile);
        deleteWithoutThrow(outputFile);

        return resultBytes;
    }

    public boolean isSupported(String inputFormat) {
        return ACCEPTABLE_INPUT_OUTPUT.get(inputFormat) != null;
    }

    public boolean isIncompatible(String inputFormat, String outputFormat) {
        return !isSupported(inputFormat) || !ACCEPTABLE_INPUT_OUTPUT.get(inputFormat).contains(outputFormat);
    }
}

