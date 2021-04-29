package com.encircle360.oss.docsrabbit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "DocsRabbit API Documentation",
                version = "v0.4",
                license = @License(name = "License", url = "https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/blob/master/LICENSE"),
                description = "DocsRabbit - Render templates the way you want. Scan documents of many standard file types.",
                contact = @Contact(
                        name = "encircle360 GmbH",
                        url = "https://www.encircle360.com"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:50005",
                        description = "local API"
                )
        }
)

@Configuration
public class APIConfig {
}
