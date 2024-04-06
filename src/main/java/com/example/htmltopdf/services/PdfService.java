package com.example.htmltopdf.services;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
@Log4j2
public class PdfService {
    @Autowired
    ResourceLoader resourceLoader;
    @Value("${input.css}")
    String cssDirectory;

    public byte[] convertHtmlFromResource(String htmlFilePath){
        // Load HTML content from the resource folder
        Resource resource = resourceLoader.getResource("file:"+htmlFilePath);
        String htmlContent;
        if(resource.exists()){
            //read the content from file
            try{
                Reader reader = new InputStreamReader(resource.getInputStream(),StandardCharsets.UTF_8);
                htmlContent = FileCopyUtils.copyToString(reader);
                return convertHtmlToPdfWithCss(htmlContent);
            }catch (IOException e){
                log.error(e.getMessage());
            }
        }else {
            log.error("Html file not found in: "+htmlFilePath);
        }
        return null;

    }

    public byte[] convertHtmlToPdfWithCss(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            // Extract CSS file path from HTML content
            String cssFilePath = extractCssFilePath(htmlContent);
            // Load CSS content
            String cssContent = loadCssContent(cssFilePath);
            // Inline CSS into HTML content
            String htmlWithInlineCss = inlineCss(htmlContent, cssContent);
            renderer.setDocumentFromString(htmlWithInlineCss);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
           log.error(e.getMessage());
        }
        return null;
    }

    public void savePdf(byte[] pdfBytes, String outputPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(pdfBytes);
        }
    }
    private String extractCssFilePath(String htmlContent) {
        log.info("Extract CSS file path");
        Pattern pattern = Pattern.compile("<link[^>]+href=\"([^\"]+)\"[^>]*>");
        Matcher matcher = pattern.matcher(htmlContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String loadCssContent(String cssFilePath) throws IOException {
        log.info("Load CSS content");
        if (cssFilePath != null) {
            String resourcePath = cssDirectory + cssFilePath;
            // Check if the CSS file is an external resource or located in the project folder
            Resource cssResource = new ClassPathResource(resourcePath);
            if (cssResource.exists()) {
                try (InputStream inputStream = cssResource.getInputStream()) {
                    return new String(StreamUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);
                }
            }else{
                log.error("Resource not found");
            }
        }
        return null;
    }

    private String inlineCss(String htmlContent, String cssContent) {
        log.info("Insert styles in HTML file");
        if (cssContent != null) {
            // Replace <link> with inline <style>
            return htmlContent.replaceFirst("<link[^>]+href=\"([^\"]+)\"[^>]*>", "<style type=\"text/css\">" + cssContent + "</style>");
        }
        return htmlContent;
    }
}
