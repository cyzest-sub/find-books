package com.cyzest.findbooks.searcher;

import lombok.Data;

import java.util.List;

@Data
public class BookSearchResult {

    private int totalCount = 0;
    private List<BookInfo> bookInfos;

}
