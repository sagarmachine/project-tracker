package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.UserRegister;
import com.highbrowape.demo.repository.UserRepository;
import com.highbrowape.demo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/oauth2/authorization/facebook")
@Slf4j
public class OAuth2FacebookController {


    String redirectUrl="http://localhost:8085/oauth2/authorization/facebook/callback";
    FacebookConnectionFactory connectionFactory= new FacebookConnectionFactory("430546587907477","04f5ae05e3de1e8965d578e85f942ed0");
    OAuth2Operations oAuth2Operations= connectionFactory.getOAuthOperations();

    @Autowired
    UserRepository userRepository;

    @Autowired
    IUserService userService;

    @Value("${frontend.auth.facebook.redirect}")
    String facebookRedirect;


    @Value("${frontend.url}")
    String frontendUrl;


    @RequestMapping("/")
    public ModelAndView connectFacebook(){

         OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirectUrl);
        params.set("namespace","project_tracker");

        String authorizeUrl= oAuth2Operations.buildAuthenticateUrl(params);

        System.out.println(authorizeUrl);
        RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
                true);
        return new ModelAndView(redirectView);

    }

    @RequestMapping("/callback")
    void facebookCallback(@RequestParam("code")String code, HttpServletResponse httpServletResponse) throws IOException {

        MultiValueMap<String, String> multiValueMap= new LinkedMultiValueMap<>();
        multiValueMap.add("client_id","430546587907477");
        multiValueMap.add("client_secret","04f5ae05e3de1e8965d578e85f942ed0");

        AccessGrant accessGrant = oAuth2Operations.exchangeForAccess(
                code,redirectUrl, multiValueMap);
        String userAccessToken = accessGrant.getAccessToken();

        Connection<Facebook> connection=connectionFactory.createConnection(accessGrant);

        Facebook facebook = connection.getApi();
        //Change
        String [] fields = { "id", "email",  "first_name", "last_name","cover", "picture" };

        HashMap userProfile = facebook.fetchObject("me", HashMap.class,fields);


        HashMap picture=(HashMap) userProfile.get("picture");
        HashMap data=(HashMap) picture.get("data");

        String email= (userProfile.get("email")!=null?userProfile.get("email").toString():"facebook@"+userProfile.get("id").toString());

        HttpHeaders headers=null;
   if(userRepository.existsByEmail(email)){
       log.error("login");
       headers=userService.createResponseForAuthenticatedUser(email).getHeaders();
   }
   else {
       log.error("register");

       UserRegister userRegister = UserRegister.builder()
               .firstName(userProfile.get("first_name").toString())
               .lastName(userProfile.get("last_name").toString())
               .email(email)
               .imageUrl(data.get("url").toString()).build();

        headers= userService.register(userRegister).getHeaders();

    }

        httpServletResponse.sendRedirect(frontendUrl+facebookRedirect
                +"/"+headers.get("Authorization")
                +"/"+headers.get("Email")
                +"/"+headers.get("Name")
//                + "/" + headers.get("ImageUrl")
        );
    }


}
