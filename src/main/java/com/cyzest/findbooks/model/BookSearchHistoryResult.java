package com.cyzest.findbooks.model;

import lombok.Data;

import java.util.List;

@Data
public class BookSearchHistoryResult {

    private int totalCount = 0;
    private List<BookSearchHistoryInfo> searchHistoryInfos;

}
