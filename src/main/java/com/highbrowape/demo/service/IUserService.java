package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.UserLogin;
import com.highbrowape.demo.dto.UserRegister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {


    ResponseEntity<?> register(UserRegister userRegister);

    ResponseEntity<?> authenticateUser(UserLogin userLogin);
}
