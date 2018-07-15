package com.cyzest.findbooks.searcher.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class KakaoBookSearchResult {

    private List<KakaoBookInfo> documents;
    private JsonNode meta;

}
