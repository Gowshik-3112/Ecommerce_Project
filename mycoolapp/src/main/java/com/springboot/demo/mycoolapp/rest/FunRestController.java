package com.springboot.demo.mycoolapp.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {

    @Value("${coach.name}")
    public String coachName;

    @Value("${team.name}")
    public String teamName;

    @GetMapping("/teaminfo")
    public String teamInfo() {
        return "Coach:" + coachName + " Team:" + teamName;
    }

    @GetMapping("/")
    public String sayHello() {
        return "Hello World";
    }
}
