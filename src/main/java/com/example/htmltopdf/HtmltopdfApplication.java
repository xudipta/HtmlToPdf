package com.example.htmltopdf;

import com.example.htmltopdf.controllers.HtmlController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class HtmltopdfApplication {

	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(HtmltopdfApplication.class, args);

		HtmlController controller = context.getBean(HtmlController.class);
		;

		controller.convertThymeleafToHtml();
	}


}
