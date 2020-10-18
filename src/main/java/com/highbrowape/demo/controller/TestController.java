package com.highbrowape.demo.controller;


import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/principaltest")
    public String test1(@RequestParam("message")String message)
    {
        return message;

    }

}
