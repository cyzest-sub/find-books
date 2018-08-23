package com.cyzest.findbooks.searcher;

import io.github.cyzest.commons.spring.dao.apt.JpaEnumCodeConverter;
import io.github.cyzest.commons.spring.model.EnumCode;

import java.util.Arrays;
import java.util.List;

@JpaEnumCodeConverter
public enum OpenApiType implements EnumCode {

    KAKAO("kakao", "카카오"),
    NAVER("naver", "네이버");

    private String code;
    private String description;

    OpenApiType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static List<OpenApiType> getOpenApiTypes() {
        return Arrays.asList(OpenApiType.KAKAO, OpenApiType.NAVER);
    }

}
