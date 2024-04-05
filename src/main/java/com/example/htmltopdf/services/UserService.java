package com.example.htmltopdf.services;

import com.example.htmltopdf.model.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    public List<User> getUsers() {
        return Arrays.asList(new User(1, "John Doe", "john@example.com"),
                new User(2, "Jane Smith", "jane@example.com"),
                new User(3, "Michael Johnson", "michael@example.com"));
    }

}
