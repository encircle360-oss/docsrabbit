package com.encircle360.oss.docsrabbit.config;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OCRConfiguration {

    @Bean
    Parser ocrParser() {
        return new AutoDetectParser();
    }

    @Bean
    ParseContext parseContext(Parser ocrParser) {
        TesseractOCRConfig config = new TesseractOCRConfig();
        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(true);

        ParseContext parseContext = new ParseContext();
        parseContext.set(TesseractOCRConfig.class, config);
        parseContext.set(PDFParserConfig.class, pdfConfig);

        // need to add this to make sure recursive parsing happens!
        parseContext.set(Parser.class, ocrParser);

        return parseContext;
    }
}
