package com.encircle360.oss.docsrabbit.integration.xls;

import com.encircle360.oss.docsrabbit.AbstractTest;
import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderResultDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Base64;
import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FileSystemRenderTest extends AbstractTest {

    @Test
    public void render_test() throws Exception {
        HashMap<String, JsonNode> model = new HashMap<>();
        model.put("testvar", DoubleNode.valueOf(0.4));

        String templateId = getTemplate("test","asdsd ${testvar}");

        RenderRequestDTO renderRequestDTO = RenderRequestDTO.builder()
                .format(RenderFormatDTO.HTML)
                .templateId(templateId)
                .model(model)
                .build();


        MvcResult renderMvcResult = post("/render", renderRequestDTO, status().isOk());
        RenderResultDTO renderResult = resultToObject(renderMvcResult, RenderResultDTO.class);
        String content = new String(Base64.getDecoder().decode(renderResult.getBase64()));
        Assertions.assertEquals("asdsd 0,4", content);
        Assertions.assertEquals("text/html", renderResult.getMimeType());

        model = new HashMap<>();
        renderRequestDTO.setModel(model);

        MvcResult exceptionResult = post("/render", renderRequestDTO, status().isInternalServerError());
        String exception = exceptionResult.getResponse().getContentAsString();
        Assertions.assertNotNull(exception);
    }
}
