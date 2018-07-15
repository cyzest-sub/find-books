package com.cyzest.findbooks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Cache-Control 헤더 no-store 설정
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setCacheSeconds(0);
        registry.addInterceptor(webContentInterceptor);
    }

}
