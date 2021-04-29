package com.encircle360.oss.docsrabbit.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SwaggerUiRedirectController {

    @Value("${springdoc.swagger-ui.host}")
    private String host;

    @Value("${springdoc.swagger-ui.url}")
    private String swaggerUiUrl;

    @GetMapping({"", "/"})
    public void method(HttpServletResponse response) {
        String path = Map.of(
                "tagsSorter", "alpha",
                "operationsSorter", "method",
                "docExpansion", "list",
                "layout", "BaseLayout",
                //"filter", "true",
                "defaultModelsExpandDepth", "0",
                "url", host.concat(swaggerUiUrl)
        )
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        String url = host.concat("/swagger-ui/index.html?").concat(path);
        response.setHeader("Location", url);
        response.setStatus(302);
    }
}
