package com.cyzest.findbooks;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ConfigTest {

    @Autowired
    private OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void OkHttp3ClientHttpRequestFactory가_빈으로_등록되어_있다() {
        Assertions.assertNotNull(okHttp3ClientHttpRequestFactory);
    }

    @Test
    public void RestTemplate이_빈으로_등록되어_있다() {
        Assertions.assertNotNull(restTemplate);
        Assertions.assertTrue(restTemplate.getRequestFactory() instanceof OkHttp3ClientHttpRequestFactory);
    }

    @Test
    public void PasswordEncoder가_빈으로_등록되어_있다() {
        Assertions.assertNotNull(passwordEncoder);
        Assertions.assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

}
