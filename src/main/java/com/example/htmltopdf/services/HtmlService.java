package com.example.htmltopdf.services;

import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class HtmlService {
    private final TemplateEngine templateEngine;
    @Value("classpath:templates/sample2.html")
    String templatePath2;

    public HtmlService() {
        this.templateEngine = new SpringTemplateEngine();
    }

    public String convertThymeleafToHtml(String templatePath, Map<String, Object> variables) throws IOException {

        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = resource.getInputStream();
        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process(htmlContent, context);
    }
    public void saveHtml(String htmlContent, String outputPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(htmlContent.getBytes(StandardCharsets.UTF_8));
        }
    }

}
