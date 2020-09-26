package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.UserLogin;
import com.highbrowape.demo.dto.input.UserRegister;
import com.highbrowape.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {


    @Autowired
    IUserService userService;

    @PostMapping("/register")
     public ResponseEntity<?> register(@Valid @RequestBody UserRegister userRegister){
        return userService.register(userRegister);
    }

    @PostMapping("/authenticate")
    public  ResponseEntity<?>  authenticate(@Valid @RequestBody UserLogin userLogin){

        return userService.authenticateUser(userLogin);
    }

}
