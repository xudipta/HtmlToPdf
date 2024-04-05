package com.example.htmltopdf.services;

import com.example.htmltopdf.model.Order;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
    public List<Order> getOrders() {
        return Arrays.asList(new Order(1, "John Doe", "Product A", 2),
                new Order(2, "Jane Smith", "Product B", 1),
                new Order(3, "Michael Johnson", "Product C", 3));
    }}
