package com.encircle360.oss.docsrabbit.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.XvfbConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;

/**
 * This service generates and creates PDF documents from html content.
 */
@Service
public class PdfService {

    private final static Base64.Encoder base64Encoder = Base64.getEncoder();

    public static Boolean isRunningInsideDocker() {
        try (Stream<String> stream =
            Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }

    public String generateBase64PDFDocument(String htmlContent) throws IOException, InterruptedException{
        return base64Encoder.encodeToString(generatePDFDocument(htmlContent));
    }

    public byte[] generatePDFDocument(String htmlContent) throws IOException, InterruptedException {
        Pdf pdf = new Pdf(this.getWkhtmlToPdfWrapperConfig());

        // TODO split htmlContent to pages
        // maybe use of https://stackoverflow.com/questions/1664049/can-i-force-a-page-break-in-html-printing
        pdf.addPageFromString(htmlContent);

        return pdf.getPDF();
    }

    private WrapperConfig getWkhtmlToPdfWrapperConfig() {
        WrapperConfig wc = new WrapperConfig(WrapperConfig.findExecutable());

        if (!isRunningInsideDocker()) {
            return wc;
        }

        XvfbConfig xc = new XvfbConfig();
        xc.addParams(new Param("--auto-servernum"));
        wc.setXvfbConfig(xc);
        return wc;
    }
}
