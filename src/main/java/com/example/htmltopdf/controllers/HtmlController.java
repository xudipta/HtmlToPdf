package com.example.htmltopdf.controllers;

import com.example.htmltopdf.services.HtmlService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HtmlController {

    private static final String INPUT_DIRECTORY = "templates/";
    private static final String FILE_NAME = "sample3.html";
    private static final String OUTPUT_DIRECTORY = "output/";
    private static final Logger logger = LogManager.getLogger(HtmlController.class);

    @Autowired
    private final HtmlService htmlService;

    public HtmlController(HtmlService htmlService) {
        this.htmlService = htmlService;
    }


    @GetMapping("/convertThymeleafToHtml")
    public ResponseEntity<String> convertThymeleafToHtml() {
        //for sample1.html
       /* Map<String, Object> variables = new HashMap<>();
        variables.put("message", "this the inserted data");*/
        //for sample2.html
        Map<String, Object> variables = new HashMap<>();
        variables.put("pageTitle", "Welcome to My Page");
        variables.put("pageDescription", "This is a sample Thymeleaf page.");
        variables.put("items", Arrays.asList("Item 1", "Item 2", "Item 3"));
        variables.put("showFooter", true);
        variables.put("message", "this the inserted data");


        try {
            logger.info("Conversion Thymeleaf -> HTML");
            // Convert Thymeleaf template to HTML
            String htmlContent=htmlService.convertThymeleafToHtml(INPUT_DIRECTORY + FILE_NAME, variables);
            String outputPath = OUTPUT_DIRECTORY + FILE_NAME;
            logger.info("htmlContent generated");
            // save html
            logger.info("Saving -> HTML");
            htmlService.saveHtml(htmlContent,outputPath);
            return ResponseEntity.ok("HTML conversion done");
        }  catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving PDF.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting HTML to PDF.");
        }
    }

}
