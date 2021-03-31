package com.encircle360.oss.docsrabbit.service.format;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.XvfbConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Stream;

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

    public String generateBase64PDFDocument(String htmlContent, String htmlHeader, String htmlFooter) throws IOException, InterruptedException {
        return base64Encoder.encodeToString(generatePDFDocument(htmlContent, htmlHeader, htmlFooter));
    }

    public byte[] generatePDFDocument(String htmlContent, String htmlHeader, String htmlFooter) throws IOException, InterruptedException {
        File htmlHeaderFile = null;
        File htmlFooterFile = null;
        Pdf pdf = new Pdf(this.getWkhtmlToPdfWrapperConfig());
        pdf.addParam(new Param("--encoding", Charset.defaultCharset().name()));
        pdf.addParam(new Param("--print-media-type"));

        // add header if given
        if (htmlHeader != null && !htmlHeader.isEmpty()) {
            htmlHeaderFile = this.writeTempHtmlFile(htmlHeader);
            pdf.addParam(new Param("--header-html", htmlHeaderFile.getAbsolutePath()));
        }

        if (htmlFooter != null && !htmlFooter.isEmpty()) {
            htmlFooterFile = this.writeTempHtmlFile(htmlFooter);
            pdf.addParam(new Param("--footer-html", htmlFooterFile.getAbsolutePath()));
        }

        pdf.addPageFromString(htmlContent);
        byte[] generatedPdfAsBytes = pdf.getPDF();

        // cleanup if needed
        if (htmlHeaderFile != null) {
            htmlHeaderFile.delete();
        }

        if (htmlFooterFile != null) {
            htmlFooterFile.delete();
        }

        return generatedPdfAsBytes;
    }

    private File writeTempHtmlFile(String content) throws IOException {
        File tmpFile = File.createTempFile("tmp_", ".html");
        tmpFile.deleteOnExit();
        FileWriter writer = new FileWriter(tmpFile, Charset.defaultCharset());
        writer.write(content);
        writer.close();
        return tmpFile;
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
