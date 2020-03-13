package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @CreateTime 2020/3/10
 * @Autor bzhang
 **/
@Controller
public class HelloController {
    @GetMapping("/hello.html")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @GetMapping("/sbag")
    @ResponseBody
    public String sbag() {
        return "sbag";
    }

    @GetMapping("/testlogin")
    public String login() {
        return "login";
    }

    @GetMapping("/demo")
    public String demo() {
        return "demo";
    }

}
