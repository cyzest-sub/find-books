package com.cyzest.findbooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(FindBooksProperties.class)
public class FindBooksApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FindBooksApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FindBooksApplication.class);
    }

}
