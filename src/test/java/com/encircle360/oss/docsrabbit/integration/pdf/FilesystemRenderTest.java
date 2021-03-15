package com.encircle360.oss.docsrabbit.integration.pdf;

import com.encircle360.oss.docsrabbit.AbstractTest;
import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FilesystemRenderTest extends AbstractTest {

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
