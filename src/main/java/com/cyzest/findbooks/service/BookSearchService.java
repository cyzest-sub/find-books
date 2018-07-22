package com.cyzest.findbooks.service;

import com.cyzest.findbooks.searcher.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class BookSearchService {

    private OpenApiBookSearchHelper openApiBookSearchHelper;

    public BookSearchResult searchBooks(OpenApiType openApiType, BookSearchParam bookSearchParam) {
        return openApiBookSearchHelper.getOpenApiBookSearcher(openApiType).search(bookSearchParam);
    }

    public BookInfo getBookByIsbn(OpenApiType openApiType, String isbn) {
        return openApiBookSearchHelper.getOpenApiBookSearcher(openApiType).searchByIsbn(isbn);
    }

}
