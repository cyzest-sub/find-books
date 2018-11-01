package com.cyzest.findbooks.searcher;

import io.github.cyzest.commons.spring.dao.apt.JpaEnumCodeConverter;
import io.github.cyzest.commons.spring.model.EnumCode;

import java.util.Arrays;
import java.util.List;

@JpaEnumCodeConverter
public enum BookSearchSort implements EnumCode {

    ACCURACY("accuracy", "정확도순"),
    LATEST("latest", "최신순");

    private String code;
    private String description;

    BookSearchSort(String code, String description) {
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

    public static List<BookSearchSort> getBookSearchSorts() {
        return Arrays.asList(BookSearchSort.ACCURACY, BookSearchSort.LATEST);
    }

}
