
package com.example.studentapp.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class StudentController {

    @GetMapping("/students")
    public List<String> getStudents() {

        return Arrays.asList(
            "Rajendra",
            "Amit",
            "Rahul",
            "Priya"
        );
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Spring Boot Application is Running!";
    }
}

