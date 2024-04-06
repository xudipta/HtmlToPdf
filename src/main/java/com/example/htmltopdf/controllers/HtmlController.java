package com.example.htmltopdf.controllers;
import com.example.htmltopdf.services.HtmlService;
import com.example.htmltopdf.services.OrderService;
import com.example.htmltopdf.services.ProductService;
import com.example.htmltopdf.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class HtmlController {
    @Value("${input.directory}")
    private String inputDirectory;
    @Value("${input.file.name}")
    private String inputFileName;
    @Value("${output.directory}")
    private String outputDirectory;

    @Autowired
    private final HtmlService htmlService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;

    public HtmlController(HtmlService htmlService) {
        this.htmlService = htmlService;
    }


    @GetMapping("/convertThymeleafToHtml")
    public ResponseEntity<String> convertThymeleafToHtml() {
        Map<String, Object> variables = getPageVariables(inputFileName.replaceAll("\\..*",""));

        try {
            // Convert Thymeleaf template to HTML
            String htmlContent=htmlService.convertThymeleafToHtml(inputDirectory + inputFileName, variables);
            log.info("Conversion Thymeleaf -> HTML");

            String outputPath = outputDirectory + inputFileName;
            log.info("htmlContent generated");

            // save html
            htmlService.saveHtml(htmlContent,outputPath);
            log.info("Saving -> HTML, Location: "+outputPath);

            return ResponseEntity.ok("HTML conversion done");

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting HTML to PDF.");
        }
    }

    private Map<String, Object> getPageVariables(String page){
        Map<String,Object> data = new HashMap<>();
        switch (page){
            case "sample1":
                data.put("message", "this the inserted data");
                break;
            case "sample2":
                data.put("pageTitle", "Welcome to My Page");
                data.put("pageDescription", "This is a sample Thymeleaf page.");
                data.put("items", Arrays.asList("Item 1", "Item 2", "Item 3"));
                data.put("showFooter", true);
                data.put("message", "this the inserted data");
                break;
            case "sample3":
                data.put("pageTitle", "Welcome to My Page");
                data.put("pageDescription", "This is a sample Thymeleaf page.");
                data.put("items", Arrays.asList("Item 1", "Item 2", "Item 3"));
                data.put("showFooter", true);
                data.put("message", "this the inserted data");
                break;
            case "sample4":
            case "sample5":
                data.put("users", userService.getUsers());
                data.put("products", productService.getProducts());
                data.put("orders", orderService.getOrders());
                boolean showOrders = false;
                data.put("showOrders", showOrders);
        }
        return data;
    }

}
