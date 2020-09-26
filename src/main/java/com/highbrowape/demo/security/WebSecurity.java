package com.highbrowape.demo.security;

import com.highbrowape.demo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@Slf4j
public class WebSecurity extends WebSecurityConfigurerAdapter {


    @Autowired
    AuthenticationEntryPointImp authenticationEntryPoint;

    @Autowired
    AuthenticationFilter authenticationFilter;

    @Autowired
    IUserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // .anyRequest().permitAll();
                 .antMatchers("/**/*")
                //.antMatchers("/api/v1/content/**/*","/api/v1/content","/api/v1/client","/api/v1/admin/authenticate","/api/v1/admin/passwordReset")
                .permitAll()
                .anyRequest().authenticated();
                //http.formLogin().loginProcessingUrl("/api/v1/admin/authenticate");
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().csrf().disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
             .and().addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//                .oauth2Login().successHandler(oAuth2SuccessHandler).failureHandler(oAuth2FailureHandler);//.defaultSuccessUrl("/api/v1/client/oauthAuthorization");
        http.cors();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(getBCryptPasswordEnc());
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEnc(){

        return new BCryptPasswordEncoder();
    }




    @Bean
    public AuthenticationManager getAuthenticationManager(){
        try {
            return super.authenticationManagerBean();
        } catch (Exception e) {
            log.info("ERRROR On GETTING AUTHENTICATION MANAGER");
        }
        return null;
    }

}
