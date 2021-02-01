package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.UserLogin;
import com.highbrowape.demo.dto.input.UserRegister;
import com.highbrowape.demo.repository.UserInteractionRepository;
import com.highbrowape.demo.repository.UserRepository;
import com.highbrowape.demo.service.IInsightService;
import com.highbrowape.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    @Autowired
    IUserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInteractionRepository userInteractionRepository;

    @Autowired
    IInsightService insightService;

    @PostMapping("/register")
     public ResponseEntity<?> register(@Valid @RequestBody UserRegister userRegister){
        return userService.register(userRegister);
    }

    @PostMapping("/authenticate")
    public  ResponseEntity<?>  authenticate(@Valid @RequestBody UserLogin userLogin){

        return userService.authenticateUser(userLogin);
    }

    @GetMapping("/my_profile")
    Object getMyProfile(Principal principal){
        return userRepository.findByEmail(principal.getName()).get();
    }

    @GetMapping("/{search}/{pageNumber}")
    public ResponseEntity<?> getUsers(@PathVariable("search")String search, @PathVariable("pageNumber")int pageNumber){

        search= search.trim();

          Pageable pageable= PageRequest.of(pageNumber,2 , Sort.by(Sort.Direction.DESC,"addedOn"));
          HashMap<String, Object> result= new HashMap<>();


        if(!search.contains(" ")) {
           result.put("content",userRepository.findEmailBySearch(search, pageable));
           result.put("totalPages",Math.ceil(userRepository.countEmailBySearch(search)/2.0));
        }
           else {
            result.put("content", userRepository.findEmailByName(search.substring(0, search.indexOf(" ")), search.substring(search.indexOf(" ") + 1), pageable));
            result.put("totalPages",Math.ceil(userRepository.countEmailByName(search.substring(0, search.indexOf(" ")), search.substring(search.indexOf(" ") + 1))/2.0));

        }

        return new ResponseEntity<>(result,HttpStatus.OK);

    }

    @GetMapping("/charts")
    public ResponseEntity<?> getChartData(Principal principal){

        HashMap<String, Object> data= insightService.getUSerCommentChartData(principal.getName());

            data.put("interaction",userInteractionRepository.findByUserEmail(principal.getName()));


        return  ResponseEntity.ok(data);
    }

}
