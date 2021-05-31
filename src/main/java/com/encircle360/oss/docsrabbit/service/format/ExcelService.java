package com.encircle360.oss.docsrabbit.service.format;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xml.XmlAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.encircle360.oss.docsrabbit.service.template.DocsRabbitTemplateLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This service generates and creates Excel documents.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {
    private final DocsRabbitTemplateLoader templateLoader;

    private final ObjectMapper objectMapper;
    private final Base64.Encoder base64Encoder = Base64.getEncoder();

    public String generateBase64ExcelDocument(@NonNull final String xmlConfiguration, final String templateId, final HashMap<String, JsonNode> jsonModel) throws IOException {
        JsonNode sheets = jsonModel.get("sheets");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HashMap<String, Object> models = new HashMap<>();
        if (sheets instanceof ArrayNode) {
            for (JsonNode sheet : sheets) {
                models.put(sheet.get("name").asText(), objectMapper.convertValue(sheet, Object.class));
            }
        } else {
            models.put("Result", objectMapper.convertValue(jsonModel, Object.class));
        }

        this.writeXls(xmlConfiguration, templateId, outputStream, models);

        return base64Encoder.encodeToString(outputStream.toByteArray());
    }

    private void writeXls(final String xmlConfiguration, final String templateId, final OutputStream outputStream, final HashMap<String, Object> models)
        throws IOException {
        InputStream inputStream = templateLoader.getFileResource(templateId + ".xlsx").getInputStream();

        Transformer transformer = TransformerFactory.createTransformer(inputStream, outputStream);
        InputStream configInputStream = new ByteArrayInputStream(xmlConfiguration.getBytes());

        AreaBuilder areaBuilder = new XmlAreaBuilder(configInputStream, transformer);
        List<Area> xlsAreaList = areaBuilder.build();

        if (xlsAreaList == null || xlsAreaList.isEmpty()) {
            log.error("No XLS area found!");
            return;
        }

        Area xlsArea = xlsAreaList.get(0);
        for (String sheetName : models.keySet()) {
            Context context = new Context();
            context.putVar("model", models.get(sheetName));
            xlsArea.applyAt(new CellRef(sheetName + "!A1"), context);
            xlsArea.processFormulas();
        }

        transformer.deleteSheet("Template");
        transformer.write();
    }

}
