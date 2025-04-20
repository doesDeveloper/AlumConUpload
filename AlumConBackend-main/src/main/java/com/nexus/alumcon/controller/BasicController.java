package com.nexus.alumcon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")
public class BasicController {
    @GetMapping("/active")
    public String hello() {
        return String.format("Hello, %s!!!", "Server is running");
    }

}
