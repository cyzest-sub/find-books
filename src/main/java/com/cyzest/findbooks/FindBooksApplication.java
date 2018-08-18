package com.cyzest.findbooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FindBooksProperties.class)
public class FindBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindBooksApplication.class, args);
    }

}
