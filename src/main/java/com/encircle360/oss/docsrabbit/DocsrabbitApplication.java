package com.encircle360.oss.docsrabbit;

import static freemarker.template.Configuration.VERSION_2_3_28;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.encircle360.oss.docsrabbit.wrapper.JsonNodeObjectWrapper;

@SpringBootApplication
public class DocsrabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocsrabbitApplication.class, args);
    }

    @Bean
    JsonNodeObjectWrapper jsonNodeObjectWrapper() {
        return new JsonNodeObjectWrapper(VERSION_2_3_28);
    }
}
