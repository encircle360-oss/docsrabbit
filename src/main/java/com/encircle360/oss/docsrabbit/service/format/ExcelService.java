package com.encircle360.oss.docsrabbit.service.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xml.XmlAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

/**
 * This service generates and creates Excel documents.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {
    private final ObjectMapper objectMapper;
    private final Base64.Encoder base64Encoder = Base64.getEncoder();

    public String generateBase64ExcelDocument(@NonNull final String xmlConfiguration, final String templateId, final HashMap<String, JsonNode> jsonModel) throws IOException {
        Object model = objectMapper.convertValue(jsonModel, Object.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.writeXls(xmlConfiguration, templateId, outputStream, model);

        return base64Encoder.encodeToString(outputStream.toByteArray());
    }


    private void writeXls(final String xmlConfiguration, final String templateId, final OutputStream outputStream, final Object model) throws IOException {
        InputStream inputStream = new ClassPathResource("templates/" + templateId + ".xlsx").getInputStream();

        Context context = new Context();
        context.putVar("model", model);

        Transformer transformer = PoiTransformer.createTransformer(inputStream, outputStream);
        InputStream configInputStream = new ByteArrayInputStream(xmlConfiguration.getBytes());

        AreaBuilder areaBuilder = new XmlAreaBuilder(configInputStream, transformer);
        List<Area> xlsAreaList = areaBuilder.build();
        Area xlsArea = xlsAreaList.get(0);
        xlsArea.applyAt(new CellRef("Result!A1"), context);
        xlsArea.processFormulas();

        transformer.deleteSheet("Template");
        transformer.write();
    }
}
