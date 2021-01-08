package com.lsc.content.distribution.contentdistribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ContentDistributionApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ContentDistributionApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ContentDistributionApplication.class, args);
    }




}
