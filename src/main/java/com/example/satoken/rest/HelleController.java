package com.example.satoken.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelleController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
