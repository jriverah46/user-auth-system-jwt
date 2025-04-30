package com.jr.security_no_guide.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControllerDemo {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    };

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "helloSecured";
    };

    @GetMapping("/hellodouble")
    public String hellodouble(){
        return "hello double";
    }
}
