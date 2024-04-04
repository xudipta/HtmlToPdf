package com.example.htmltopdf.services;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class PdfService {


    @Autowired
    ResourceLoader resourceLoader;


    public byte[] convertHtmlToPdfFile(String htmlFilePath) throws DocumentException, IOException {
        // Load HTML content from the resource folder
        ClassPathResource resource = new ClassPathResource(htmlFilePath);
        InputStream inputStream = resource.getInputStream();
        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        // Convert HTML to PDF
        //return convertHtmlToPdf(htmlContent);
        return convertHtmlToPdfWithCss(htmlContent);
    }
    public byte[] convertHtmlFromResource(String htmlFilePath) throws DocumentException{
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

            }
        }else {
            System.out.println("test");
        }
        return null;

    }

    public byte[] convertHtmlToPdf(String htmlContent) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Parse HTML content and add elements to the document
        HTMLWorker htmlWorker = new HTMLWorker(document);
        htmlWorker.parse(new StringReader(htmlContent));

        document.close();

        return outputStream.toByteArray();
    }

    public byte[] convertHtmlToPdfWithCss(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void savePdf(byte[] pdfBytes, String outputPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(pdfBytes);
        }
    }
}
