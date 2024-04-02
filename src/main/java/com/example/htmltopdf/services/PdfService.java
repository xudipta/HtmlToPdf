package com.example.htmltopdf.services;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class PdfService {

    public byte[] convertHtmlToPdfFile(String htmlFilePath) throws DocumentException, IOException {
        // Load HTML content from the resource folder
        ClassPathResource resource = new ClassPathResource(htmlFilePath);
        InputStream inputStream = resource.getInputStream();
        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        // Convert HTML to PDF
        return convertHtmlToPdf(htmlContent);
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

    public void savePdf(byte[] pdfBytes, String outputPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(pdfBytes);
        }
    }
}
