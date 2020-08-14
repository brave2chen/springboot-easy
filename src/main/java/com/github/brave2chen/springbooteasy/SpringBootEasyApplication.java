package com.github.brave2chen.springbooteasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author brave2chen
 */
@RestController
@SpringBootApplication
public class SpringBootEasyApplication {

    @GetMapping("")
    public String hello() {
        return "Hello SpringBootEasy";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootEasyApplication.class, args);
    }
}
