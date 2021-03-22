package com.encircle360.oss.docsrabbit;

import com.encircle360.oss.docsrabbit.wrapper.JsonNodeObjectWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static freemarker.template.Configuration.VERSION_2_3_28;

@SpringBootApplication(exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class,
    DataSourceAutoConfiguration.class
})
public class DocsrabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocsrabbitApplication.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages", "file:/resources/i18n/messages", "i18n/messages");
        messageSource.setDefaultLocale(Locale.ENGLISH);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

        return messageSource;
    }

    @Bean
    JsonNodeObjectWrapper jsonNodeObjectWrapper() {
        return new JsonNodeObjectWrapper(VERSION_2_3_28);
    }
}
