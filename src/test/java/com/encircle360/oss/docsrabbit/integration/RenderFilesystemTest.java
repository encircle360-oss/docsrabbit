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
import com.encircle360.oss.docsrabbit.dto.render.InlineRenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.docsrabbit.dto.template.TemplateDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;

@SpringBootTest
public class RenderFilesystemTest extends AbstractTest {

    @Test
    public void render_include() throws Exception {
        HashMap<String, JsonNode> model = new HashMap<>();
        model.put("default", DoubleNode.valueOf(0.4));
        RenderRequestDTO request = RenderRequestDTO
            .builder()
            .templateId("include")
            .format(RenderFormatDTO.HTML)
            .locale("de")
            .model(model)
            .build();

        MvcResult renderMvcResult = post("/render", request, status().isOk());
    }

}
