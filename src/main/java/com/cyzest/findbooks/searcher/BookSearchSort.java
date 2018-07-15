package com.cyzest.findbooks.searcher;

import com.cyzest.findbooks.common.EnumCode;

import java.util.Arrays;
import java.util.List;

public enum BookSearchSort implements EnumCode {

    ACCURACY("accuracy", "정확도순"),
    RECENCY("recency", "최신순"),
    SALES("sales", "판매량순");

    private String code;
    private String description;

    private BookSearchSort(String code, String description) {
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
        return Arrays.asList(BookSearchSort.ACCURACY, BookSearchSort.RECENCY, BookSearchSort.SALES);
    }

}
