package com.highbrowape.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/principaltest")
    public String test1(Principal principal){
        return principal.getName();
    }

}
