package com.example.htmltopdf.controllers;
import com.example.htmltopdf.services.PdfService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@Log4j2
public class PdfController {

    @Value("${output.directory}")
    private String inputDirectory;
    @Value("${input.file.name}")
    private String inputFileName;
    @Value("${output.directory}")
    private String outputDirectory;


    @Autowired
    PdfService pdfService;

    @RequestMapping("/getPdf")
    public ResponseEntity<String> convertHtmlFileToPdf() {
        try {
            // Convert HTML to PDF
            byte[] pdfBytes = pdfService.convertHtmlFromResource(inputDirectory + inputFileName);
            log.info("Conversion HTML -> PDF");

            // Specify the output file path
            String outputPath = outputDirectory + inputFileName.replace(".html", ".pdf");

            // Save PDF to specified output path
            pdfService.savePdf(pdfBytes, outputPath);
            log.info("Saving -> PDF, Location: "+outputPath);

            return ResponseEntity.ok("PDF saved successfully at: " + outputPath);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving PDF.");
        } catch (Exception e) {
            //logger.error(e.getMessage());
            log.error((e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting HTML to PDF.");
        }
    }

}
