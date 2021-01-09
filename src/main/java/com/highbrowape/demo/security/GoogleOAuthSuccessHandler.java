package com.highbrowape.demo.security;

import com.highbrowape.demo.dto.input.UserRegister;
import com.highbrowape.demo.entity.User;
import com.highbrowape.demo.repository.UserRepository;
import com.highbrowape.demo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Component
@Slf4j
public class GoogleOAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    IUserService userService;

    @Autowired
    UserRepository userRepository;

    @Value("${frontend.auth.google.redirect}")
    String googleRedirect;

    @Value("${frontend.auth.github.redirect}")
    String githubRedirect;


    @Value("${frontend.url}")
    String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;

        log.info("OAUTH successfull ------> "+token.getPrincipal().getAttributes());
//        log.info(token.getPrincipal().getAttributes().get("given_name").toString());
//        log.info(token.getPrincipal().getAttributes().get("family_name").toString());
//        log.info(token.getPrincipal().getAttributes().get("email").toString());


        if(token.getPrincipal().getAttributes().get("email")==null||token.getPrincipal().getAttributes().get("given_name")==null) {
            onGithubSuccessAuthentication(httpServletRequest, httpServletResponse, authentication);
            return ;
        }


     Map<String,Object> map=token.getPrincipal().getAttributes();
        String email= token.getPrincipal().getAttributes().get("email").toString();
        HttpHeaders headers=null;
        if(userRepository.existsByEmail(email)) {

        log.error("login");
            headers=userService.createResponseForAuthenticatedUser(email).getHeaders();
        }

        else {
            log.error("register");
          Map<String,Object> attributes=token.getPrincipal().getAttributes();
            UserRegister userRegister= UserRegister.builder()
                    .firstName(attributes.get("given_name").toString())
                    .lastName(attributes.get("family_name").toString())
                    .email(attributes.get("email").toString())
                    .imageUrl(attributes.get("picture").toString()).build();

           headers= userService.register(userRegister).getHeaders();
       }
        httpServletResponse.sendRedirect(frontendUrl+googleRedirect
                +"/"+headers.get("Authorization")
                +"/"+headers.get("Email")
                +"/"+headers.get("Name")
//                + "/" + headers.get("ImageUrl")
        );    }

    public void onGithubSuccessAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException{

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;

        Map<String,Object> map=token.getPrincipal().getAttributes();
        String email= token.getPrincipal().getAttributes().get("email")==null?"github@"+token.getPrincipal().getAttributes().get("id").toString():token.getPrincipal().getAttributes().get("email").toString();
        HttpHeaders headers=null;
        if(userRepository.existsByEmail(email)) {

            log.error("login");
            headers=userService.createResponseForAuthenticatedUser(email).getHeaders();
        }

        else {
            log.error("register");
            Map<String,Object> attributes=token.getPrincipal().getAttributes();
            UserRegister userRegister= UserRegister.builder()
                    .firstName(attributes.get("login").toString())
                    //.lastName(attributes.get("family_name").toString())
                    .email(email)
                    .imageUrl(attributes.get("avatar_url").toString()).build();

            headers= userService.register(userRegister).getHeaders();
        }
        httpServletResponse.sendRedirect(frontendUrl+githubRedirect
                +"/"+headers.get("Authorization")
                +"/"+headers.get("Email")
                +"/"+headers.get("Name")
//                + "/" + headers.get("ImageUrl")
        );    }
}
