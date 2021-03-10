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
@ActiveProfiles(MongoDbConfig.PROFILE)
public class RenderTest extends AbstractTest {

    @Test
    public void render_test() throws Exception {
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
        Assertions.assertEquals("text/html", renderResult.getMimeType());

        model = new HashMap<>();
        request.setModel(model);

        MvcResult exceptionResult = post("/render", request, status().isInternalServerError());
        String exception = exceptionResult.getResponse().getContentAsString();
        Assertions.assertNotNull(exception);
    }

    @Test
    public void render_include() throws Exception {
        HashMap<String, JsonNode> model = new HashMap<>();
        model.put("testvar", DoubleNode.valueOf(0.4));
        RenderRequestDTO request = RenderRequestDTO.builder().build();

        String templateId = getTemplate();
        request.setFormat(RenderFormatDTO.HTML);
        request.setTemplateId(templateId);
        request.setModel(model);

        MvcResult renderMvcResult = post("/render", request, status().isOk());
    }

    @Test
    public void render_inline_test() throws Exception {
        InlineRenderRequestDTO inlineRenderRequestDTO = InlineRenderRequestDTO.builder().build();
        post("/render", inlineRenderRequestDTO, status().isBadRequest());
        post("/render/inline", inlineRenderRequestDTO, status().isBadRequest());

        HashMap<String, JsonNode> model = new HashMap<>();
        model.put("test", TextNode.valueOf("yeaha!"));

        inlineRenderRequestDTO = InlineRenderRequestDTO
            .builder()
            .template("test ${test}")
            .format(RenderFormatDTO.HTML)
            .model(model)
            .locale("de")
            .build();

        MvcResult renderResult = post("/render/inline", inlineRenderRequestDTO, status().isOk());
        RenderResultDTO renderResultDTO = resultToObject(renderResult, RenderResultDTO.class);
        Assertions.assertNotNull(renderResultDTO);
        Assertions.assertNotNull(renderResultDTO.getBase64());
        Assertions.assertNotEquals("",renderResultDTO.getBase64());
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
