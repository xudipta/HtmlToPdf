package com.example.htmltopdf.services;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Log4j2
public class HtmlService {
    private final TemplateEngine templateEngine;

    public HtmlService() {
        this.templateEngine = new SpringTemplateEngine();
    }

    public String convertThymeleafToHtml(String templatePath, Map<String, Object> variables) {

        ClassPathResource resource = new ClassPathResource(templatePath);
        try {
            InputStream inputStream = resource.getInputStream();
            String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Context context = new Context();
            context.setVariables(variables);
            return templateEngine.process(htmlContent, context);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
    public void saveHtml(String htmlContent, String outputPath) {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(htmlContent.getBytes(StandardCharsets.UTF_8));
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }



}
