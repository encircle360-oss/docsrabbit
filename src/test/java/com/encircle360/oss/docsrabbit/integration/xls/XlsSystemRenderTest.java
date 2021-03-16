package com.encircle360.oss.docsrabbit.integration.xls;

import com.encircle360.oss.docsrabbit.AbstractTest;
import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class XlsSystemRenderTest extends AbstractTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void render_test() throws Exception {
        ArrayList<ObjectNode> employees = new ArrayList<>();
        for(int i = 1; i < 4; i++){
            ObjectNode employee = objectMapper.createObjectNode();
            employee.put("name", "cName" + i);
            employees.add(employee);
        }

        ArrayNode array = mapper.valueToTree(employees);
        JsonNode employeesNode = mapper.valueToTree(array);
        HashMap<String, JsonNode> model = new HashMap<>();
        model.put("employees", employeesNode);

        RenderRequestDTO renderRequestDTO = RenderRequestDTO.builder()
                .format(RenderFormatDTO.XLS)
                .templateId("xls/default")
                .containerId("default")
                .model(model)
                .build();

        super.post("/render", renderRequestDTO, status().isOk());
    }
}
