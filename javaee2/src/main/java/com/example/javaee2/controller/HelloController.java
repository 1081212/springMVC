package com.example.javaee2.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class HelloController {
    @GetMapping("/test")
    private String data() {
        return "访问成功";
    }
}

