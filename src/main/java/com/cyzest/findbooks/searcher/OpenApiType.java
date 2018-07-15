package com.cyzest.findbooks.searcher;

import com.cyzest.findbooks.common.EnumCode;

import java.util.Arrays;
import java.util.List;

public enum OpenApiType implements EnumCode {

    KAKAO("kakao", "카카오");

    private String code;
    private String description;

    private OpenApiType(String code, String description) {
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
        return Arrays.asList(OpenApiType.KAKAO);
    }

}
