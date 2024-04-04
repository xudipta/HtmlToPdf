package com.example.htmltopdf.controllers;
import com.example.htmltopdf.services.PdfService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@Log4j2
public class PdfController {

    private static final String INPUT_DIRECTORY = "templates/";
    private static final String INPUT_DIRECTORY_OUTPUT = "output/";
    private static final String FILE_NAME = "sample3.html";
    private static final String OUTPUT_DIRECTORY = "output/";


    @Autowired
    PdfService pdfService;

    @RequestMapping("/convertHtmlFileToPdf")
    public ResponseEntity<String> convertHtmlFileToPdf() {
        try {
            // Convert HTML to PDF
            //byte[] pdfBytes = pdfService.convertHtmlToPdfFile(INPUT_DIRECTORY + FILE_NAME);
            byte[] pdfBytes = pdfService.convertHtmlFromResource(INPUT_DIRECTORY_OUTPUT + FILE_NAME);
            log.info("Conversion HTML -> PDF");

            // Specify the output file path
            String outputPath = OUTPUT_DIRECTORY + FILE_NAME.replace(".html", ".pdf");

            // Save PDF to specified output path
            pdfService.savePdf(pdfBytes, outputPath);
            log.info("Saving -> PDF, Location: "+outputPath);

            return ResponseEntity.ok("PDF saved successfully at: " + outputPath);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving PDF.");
        } catch (Exception e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting HTML to PDF.");
        }
    }

}
