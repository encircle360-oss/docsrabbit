package com.encircle360.oss.docsrabbit.service;

import lombok.extern.slf4j.Slf4j;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This service generates and creates Excel documents.
 */
@Slf4j
@Service
public class ExcelService {

    public String generateBase64ExcelDocument(@NonNull final String templatePath) throws IOException {
        log.info("Running Object Collection demo");

        //List<Employee> employees = generateSampleEmployeeData();

        try (InputStream is = new ClassPathResource("templates/xls/default.xlsx").getInputStream()) {
            try (OutputStream os = new FileOutputStream("templates/xls/output.xls")) {
                Context context = new Context();
                context.putVar("employees", null);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }

        return null;
    }
}

// ${employee.name}	${employee.birthDate}	${employee.payment}	${employee.bonus}
