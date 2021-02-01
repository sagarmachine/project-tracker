package com.highbrowape.demo.security;

import com.highbrowape.demo.service.IInsightService;
import com.highbrowape.demo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    IUserService userService;

    @Autowired
   JWTUtil jwtUtil;

    @Autowired
    IInsightService insightService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization= httpServletRequest.getHeader("Authorization");

        log.error("JWT1");
        if(authorization!=null && authorization.length()>7){
            String jwtToken = authorization.substring(7);
            String adminId = jwtUtil.getUsernameFromToken(jwtToken);
            UserDetails user =userService.loadUserByUsername(adminId);
            log.error("JWT2");
            if(user!=null && jwtUtil.validateToken(jwtToken,user) && SecurityContextHolder.getContext().getAuthentication()==null  ){
                Object userDetails;
                Object principal;
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),null,new ArrayList<>() );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContextHolder.setContext(context);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                insightService.addUserInteraction(user.getUsername(),1);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
