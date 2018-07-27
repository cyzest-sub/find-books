package com.cyzest.findbooks.model;

import lombok.Data;

@Data
public class BookMarkPagingParam {

    private BookMarkSort sort = BookMarkSort.REG_DATE;
    private Integer page = 1;
    private Integer size = 9;

}
