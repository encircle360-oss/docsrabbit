package com.encircle360.oss.docsrabbit.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.docsrabbit.AbstractTest;
import com.encircle360.oss.docsrabbit.config.MongoDbConfig;
import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.docsrabbit.dto.template.TemplateDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;

@SpringBootTest
@ActiveProfiles(MongoDbConfig.PROFILE)
public class RenderTest extends AbstractTest {

    @Test
    public void renderTest() throws Exception {
        RenderRequestDTO request = RenderRequestDTO.builder().build();

        post("/render", request, status().isBadRequest());

        HashMap<String, JsonNode> model = new HashMap<>();
        model.put("testvar", DoubleNode.valueOf(0.4));

        String templateId = getTemplate();
        request.setFormat(RenderFormatDTO.HTML);
        request.setTemplateId(templateId);
        request.setModel(model);

        MvcResult renderMvcResult = post("/render", request, status().isOk());
        RenderResultDTO renderResult = resultToObject(renderMvcResult, RenderResultDTO.class);
        String content = new String(Base64.getDecoder().decode(renderResult.getBase64()));
        Assertions.assertEquals("asdsd 0,4", content);

        model = new HashMap<>();
        request.setModel(model);
        post("/render", request, status().isInternalServerError());
    }

    private String getTemplate() throws Exception {
        CreateUpdateTemplateDTO createUpdateTemplateDTO = CreateUpdateTemplateDTO
            .builder()
            .html("asdsd ${testvar}")
            .name("test")
            .locale("de")
            .build();

        MvcResult result = post("/templates", createUpdateTemplateDTO, status().isCreated());

        TemplateDTO templateDTO = resultToObject(result, TemplateDTO.class);
        return templateDTO.getId();
    }
}
