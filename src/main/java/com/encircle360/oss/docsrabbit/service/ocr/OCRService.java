package com.encircle360.oss.docsrabbit.service.ocr;

import com.encircle360.oss.docsrabbit.dto.ocr.OCRResultDTO;
import lombok.RequiredArgsConstructor;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class OCRService {

    private final Parser ocrParser;
    private final ParseContext parseContext;

    public OCRResultDTO parse(InputStream stream) throws Exception {
        BodyContentHandler contentHandler = new BodyContentHandler(Integer.MAX_VALUE);
        Metadata metadata = new Metadata();

        ocrParser.parse(stream, contentHandler, metadata, parseContext);

        return OCRResultDTO.builder()
                .content(contentHandler.toString())
                .build();
    }
}
