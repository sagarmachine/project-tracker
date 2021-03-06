package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.input.UserLogin;
import com.highbrowape.demo.dto.input.UserRegister;
import com.highbrowape.demo.entity.User;
import com.highbrowape.demo.entity.UserInsights;
import com.highbrowape.demo.exception.InvalidCredentials;
import com.highbrowape.demo.exception.UserAlreadyExist;
import com.highbrowape.demo.repository.UserRepository;
import com.highbrowape.demo.security.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
@Slf4j
public class UserServiceImpl implements IUserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(s);

        log.info(s);

        if(userOptional.isPresent())
             return new org.springframework.security.core.userdetails.User(userOptional.get().getEmail(),userOptional.get().getPassword(),new ArrayList<>());
        else
            return null;
    }


    @Override
    public ResponseEntity<?> register(UserRegister userRegister) {

        Optional<User> userOptional= userRepository.findByEmail(userRegister.getEmail());

        if(userOptional.isPresent())
            throw new UserAlreadyExist( "email already exist");

        ModelMapper mapper =new ModelMapper();

        User user = mapper.map(userRegister,User.class);


        user.setPassword(passwordEncoder.encode((user.getPassword() == null) ? "123" : user.getPassword()));
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());



        UserInsights userInsights = new UserInsights();

        userInsights.setUser(user);
        user.setUserInsights(userInsights);

          user= userRepository.save(user);

        String jwt = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),new ArrayList<>()));

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+jwt);
        httpHeaders.add("Name",user.getFirstName()+" "+user.getLastName());
        httpHeaders.add("Email",user.getEmail());
        httpHeaders.add("ImageUrl",user.getImageUrl());
        httpHeaders.add("ThumbnailUrl",user.getThumbnailUrl());


        return new ResponseEntity<>(user,httpHeaders,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> authenticateUser(UserLogin userLogin) {

        Authentication token = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(),userLogin.getPassword(),new ArrayList<>()));

        if(token.isAuthenticated()){
            HttpHeaders httpHeaders= new HttpHeaders();
            String jwt = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(userLogin.getEmail(),userLogin.getPassword(),new ArrayList<>()));


            Optional<User> userOptional = userRepository.findByEmail(token.getName());

            httpHeaders.add("Authorization","Bearer "+jwt);
            httpHeaders.add("Name",userOptional.get().getFirstName()+" "+userOptional.get().getLastName());
            httpHeaders.add("Email",token.getName());
            httpHeaders.add("ImageUrl",userOptional.get().getImageUrl());
            httpHeaders.add("ThumbnailUrl",userOptional.get().getThumbnailUrl());

           if(userOptional.get().getUserInsights()!=null) {
                httpHeaders.add("ObjectivesCompleted", userOptional.get().getUserInsights().getCompletedObjectiveCount() + "");
                httpHeaders.add("TotalObjectives", userOptional.get().getUserInsights().getObjectiveCount() + "");
            }

            return new ResponseEntity<>(httpHeaders,HttpStatus.OK);

        }else
            throw  new InvalidCredentials("Invalid Credentials");



    }

    @Override
    public ResponseEntity<?> createResponseForAuthenticatedUser(String email) {
        User user = userRepository.findByEmail(email).get();
        String jwt = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),new ArrayList<>()));

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+jwt);
        httpHeaders.add("Name",user.getFirstName()+" "+user.getLastName());
        httpHeaders.add("Email",user.getEmail());
        httpHeaders.add("ImageUrl",user.getImageUrl());
        return new ResponseEntity<>(user,httpHeaders,HttpStatus.OK);

    }
}
