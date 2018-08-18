package com.cyzest.findbooks;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ConfigTest {

    @Autowired
    private OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void OkHttp3ClientHttpRequestFactory가_빈으로_등록되어_있다() {
        Assert.assertNotNull(okHttp3ClientHttpRequestFactory);
    }

    @Test
    public void RestTemplate이_빈으로_등록되어_있다() {
        Assert.assertNotNull(restTemplate);
        Assert.assertTrue(restTemplate.getRequestFactory() instanceof OkHttp3ClientHttpRequestFactory);
    }

}
