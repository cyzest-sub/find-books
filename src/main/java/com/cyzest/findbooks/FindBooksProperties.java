package com.cyzest.findbooks;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("findbooks")
public class FindBooksProperties {

    @Data
    public static class Searcher {
        private String kakaoApiKey;
    }

    private Searcher searcher;

}
