package com.encircle360.oss.docsrabbit.service.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xml.XmlAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
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
    private final static Base64.Encoder base64Encoder = Base64.getEncoder();

    public String generateBase64ExcelDocument(@NonNull final String processedTemplate, final String containerId, final HashMap<String, JsonNode> model) throws IOException {
        ObjectNode objectNode = objectMapper.convertValue(model, ObjectNode.class);

        FileOutputStream fos = new FileOutputStream("/Users/marcello.muscara/Development/encircle360-oss/docsrabbit/src/main/resources/output.xlsx");
        this.writeXls(processedTemplate, containerId, fos, objectNode);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.writeXls(processedTemplate, containerId, os, objectNode);

        return base64Encoder.encodeToString(os.toByteArray());
    }


    private void writeXls(final String processedTemplate, final String containerId, final OutputStream os, final JsonNode jsonModel) throws IOException {
        InputStream is = new ClassPathResource("templates/container/" + containerId + ".xlsx").getInputStream();

        Context context = new Context();
        context.putVar("model", jsonModel);

        Transformer transformer = TransformerFactory.createTransformer(is, os);
        InputStream configInputStream = new ByteArrayInputStream(processedTemplate.getBytes());

        AreaBuilder areaBuilder = new XmlAreaBuilder(configInputStream, transformer);
        List<Area> xlsAreaList = areaBuilder.build();
        Area xlsArea = xlsAreaList.get(0);
        xlsArea.applyAt(new CellRef("Result!A1"), context);
        xlsArea.processFormulas();

        transformer.deleteSheet("Template");
        transformer.write();
    }

}
