package com.encircle360.oss.docsrabbit.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.docsrabbit.AbstractTest;
import com.encircle360.oss.docsrabbit.config.MongoDbConfig;
import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.docsrabbit.dto.template.TemplateDTO;

@SpringBootTest
@ActiveProfiles(MongoDbConfig.PROFILE)
public class RenderTest extends AbstractTest {

    @Test
    public void renderTest() throws Exception {
        RenderRequestDTO request = RenderRequestDTO.builder().build();

        post("/render", request, status().isBadRequest());

        String templateId = getTemplate();
        request.setFormat(RenderFormatDTO.HTML);
        request.setTemplateId(templateId);

        post("/render", request, status().isOk());
    }

    private String getTemplate() throws Exception {
        CreateUpdateTemplateDTO createUpdateTemplateDTO = CreateUpdateTemplateDTO
            .builder()
            .html("asdsd")
            .name("test")
            .locale("de")
            .build();

        MvcResult result = post("/templates", createUpdateTemplateDTO, status().isCreated());

        TemplateDTO templateDTO = resultToObject(result, TemplateDTO.class);
        return templateDTO.getId();
    }
}
