package com.example.javaee2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.javaee2.dao")
public class Javaee2Application {

    public static void main(String[] args) {
        SpringApplication.run(Javaee2Application.class, args);
    }

}
