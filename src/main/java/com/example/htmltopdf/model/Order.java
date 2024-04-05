package com.example.htmltopdf.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private int id;
    private String user;
    private String product;
    private int quantity;
}
