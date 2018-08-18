package com.cyzest.findbooks.model;

import io.github.cyzest.commons.spring.model.EnumCode;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

public enum BookMarkSort implements EnumCode {

    REG_DATE(new Sort(Sort.Direction.DESC, "regDate"), "시간순"),
    TITLE(new Sort(Sort.Direction.ASC, "title"), "이름순");

    private Sort sort;
    private String description;

    private BookMarkSort(Sort sort, String description) {
        this.sort = sort;
        this.description = description;
    }

    @Override
    public String getCode() {
        Sort.Order order = sort.iterator().next();
        return order.getProperty() + "," + order.getDirection().name();
    }

    public String getDescription() {
        return description;
    }

    public Sort getSort() {
        return sort;
    }

    public static List<BookMarkSort> getBookMarkSorts() {
        return Arrays.asList(BookMarkSort.REG_DATE, BookMarkSort.TITLE);
    }

}
