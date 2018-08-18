package com.cyzest.findbooks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(okHttp3ClientHttpRequestFactory());
    }

}
