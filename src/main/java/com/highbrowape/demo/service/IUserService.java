package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.input.UserLogin;
import com.highbrowape.demo.dto.input.UserRegister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {


    ResponseEntity<?> register(UserRegister userRegister);

    ResponseEntity<?> authenticateUser(UserLogin userLogin);

    ResponseEntity<?> createResponseForAuthenticatedUser(String email);
}
