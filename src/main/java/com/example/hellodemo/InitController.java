package com.example.hellodemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    @GetMapping("/")
    public String index() {
        return "Greetings from HelloDemo! (a SpringBoot Application)";
    }

}
