package com.encircle360.oss.docsrabbit.integration;

import com.encircle360.oss.docsrabbit.AbstractTest;
import com.encircle360.oss.docsrabbit.config.MongoDbConfig;
import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(MongoDbConfig.PROFILE)
public class RenderTest extends AbstractTest {

    @Test
    public void render_test() throws Exception {
        HashMap<String, JsonNode> model = new HashMap<>();
        model.put("testvar", DoubleNode.valueOf(0.4));

        RenderRequestDTO request = RenderRequestDTO.builder()
                .format(RenderFormatDTO.XLS)
                .templateId("default")
                .model(model)
                .build();

        post("/render", request, status().isOk());
    }
}
