package com.example.htmltopdf.controllers;
import com.example.htmltopdf.services.HtmlService;
import com.example.htmltopdf.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PdfController {

    private static final String INPUT_DIRECTORY = "templates/";
    private static final String FILE_NAME = "sample.html";
    private static final String OUTPUT_DIRECTORY = "output/";

    @Autowired
    PdfService pdfService;


    @RequestMapping("/convertHtmlFileToPdf")
    public ResponseEntity<String> convertHtmlFileToPdf() {

        try {
            // Convert HTML to PDF
            byte[] pdfBytes = pdfService.convertHtmlToPdfFile(INPUT_DIRECTORY + FILE_NAME);

            // Specify the output file path
            String outputPath = OUTPUT_DIRECTORY + FILE_NAME.replace(".html", ".pdf");

            // Save PDF to specified output path
            pdfService.savePdf(pdfBytes, outputPath);

            return ResponseEntity.ok("PDF saved successfully at: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving PDF.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting HTML to PDF.");
        }
    }

}
