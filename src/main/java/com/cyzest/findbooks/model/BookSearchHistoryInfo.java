package com.cyzest.findbooks.model;

import com.cyzest.findbooks.searcher.BookSearchCategory;
import com.cyzest.findbooks.searcher.BookSearchSort;
import com.cyzest.findbooks.searcher.BookSearchTarget;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookSearchHistoryInfo {

    private Long id;
    private String query;
    private OpenApiType openApiType;
    private BookSearchCategory category;
    private BookSearchTarget target;
    private BookSearchSort sort;
    private LocalDateTime regDate;

}
