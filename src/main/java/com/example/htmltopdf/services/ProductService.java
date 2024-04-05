package com.example.htmltopdf.services;

import com.example.htmltopdf.model.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    public List<Product> getProducts() {
        return Arrays.asList(new Product(1, "Product A", 10.00),
                new Product(2, "Product B", 15.00),
                new Product(3, "Product C", 20.00));
    }

}
