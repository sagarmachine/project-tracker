package com.lsc.content.distribution.facebook;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class FacebookConfig {


    @Bean
    FacebookConnectionFactory getFacebookConnectionFactory(){
        System.out.println("Configuring .........................................!!!!!!!");
        return new FacebookConnectionFactory(FacebookRequestConstants.appId,FacebookRequestConstants.appPassword);
    }

}
