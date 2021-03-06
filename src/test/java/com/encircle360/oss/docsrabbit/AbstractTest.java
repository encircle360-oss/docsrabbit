package com.encircle360.oss.docsrabbit;

import com.encircle360.oss.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.docsrabbit.dto.template.TemplateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public abstract class AbstractTest {

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected MockMvc mock;

    protected MvcResult get(String url, ResultMatcher resultMatcher) throws Exception {
        return mock.perform(MockMvcRequestBuilders.get(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(resultMatcher)
            .andReturn();
    }

    protected MvcResult delete(String url, ResultMatcher resultMatcher) throws Exception {
        return mock.perform(MockMvcRequestBuilders.delete(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(resultMatcher)
            .andReturn();
    }

    protected <T> MvcResult post(String url, T body, ResultMatcher resultMatcher) throws Exception {
        return mock.perform(MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(body)))
            .andExpect(resultMatcher)
            .andReturn();
    }

    protected <T> MvcResult emptyPost(String url, ResultMatcher resultMatcher) throws Exception {
        return mock.perform(MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(resultMatcher)
            .andReturn();
    }

    protected <T> MvcResult put(String url, T body, ResultMatcher resultMatcher) throws Exception {
        return mock.perform(MockMvcRequestBuilders.put(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(body)))
            .andExpect(resultMatcher)
            .andReturn();
    }

    protected <T> T resultToObject(MvcResult result, Class<T> tClass) throws Exception {
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getContentAsString());
        return mapper.readValue(response.getContentAsString(), tClass);
    }

    protected String getTemplate(String name, String content) throws Exception {
        CreateUpdateTemplateDTO createUpdateTemplateDTO = CreateUpdateTemplateDTO
                .builder()
                .content(content)
                .name(name)
                .locale("de")
                .build();

        MvcResult result = post("/templates", createUpdateTemplateDTO, status().isCreated());

        TemplateDTO templateDTO = resultToObject(result, TemplateDTO.class);
        Assertions.assertNotNull(templateDTO.getLastUpdate());

        return templateDTO.getId();
    }
}
