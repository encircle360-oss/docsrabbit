package com.encircle360.oss.docsrabbit.service;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.XvfbConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * This service generates and creates PDF documents from html content.
 */
@Service
public class PdfService {

    private static final String TMP_INVOICE_DIR = "/tmp/invoices/";

    public static Boolean isRunningInsideDocker() {
        try (Stream<String> stream =
                     Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }

    public byte[] generatePDFDocument(String htmlContent) throws IOException, InterruptedException {
        Pdf pdf = new Pdf(this.getWkhtmlToPdfWrapperConfig());

        // TODO split htmlContent to pages
        // maybe use of https://stackoverflow.com/questions/1664049/can-i-force-a-page-break-in-html-printing
        pdf.addPageFromString(htmlContent);
        pdf.addToc();
        File pdfDocumentFile = pdf.saveAsDirect(TMP_INVOICE_DIR + UUID.randomUUID() + ".pdf");
        pdfDocumentFile.deleteOnExit();
        byte[] document = Files.readAllBytes(pdfDocumentFile.toPath());
        pdfDocumentFile.delete();

        return document;
    }

    private WrapperConfig getWkhtmlToPdfWrapperConfig() {
        XvfbConfig xc = new XvfbConfig();
        xc.addParams(new Param("--auto-servernum"));

        WrapperConfig wc = new WrapperConfig();

        if (isRunningInsideDocker()) {
            wc.setXvfbConfig(xc);
        }

        return wc;
    }
}
