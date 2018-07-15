package com.cyzest.findbooks.searcher.kakao;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Data
public class KakaoBookInfo {

    private static final String EMPTY_IMAGE_URL = "http://i1.daumcdn.net/img-contents/book/2010/72x100_v2.gif";

    private String title;
    private String contents;
    private String url;
    private String isbn;
    private Date datetime;
    private List<String> authors;
    private String publisher;
    private List<String> translators;
    private Integer price;

    @JsonProperty("sale_price")
    private Integer salePrice;

    private String category;
    private String thumbnail;
    private String barcode;

    public String getThumbnail() {
        return StringUtils.isEmpty(thumbnail) ? EMPTY_IMAGE_URL : thumbnail;
    }

}
