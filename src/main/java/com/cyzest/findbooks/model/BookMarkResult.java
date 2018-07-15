package com.cyzest.findbooks.model;

import lombok.Data;

import java.util.List;

@Data
public class BookMarkResult {

    private int totalCount = 0;
    private List<BookMarkInfo> bookMarkInfos;

}
