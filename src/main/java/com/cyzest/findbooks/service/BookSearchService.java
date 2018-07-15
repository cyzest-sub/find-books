package com.cyzest.findbooks.service;

import com.cyzest.findbooks.searcher.BookInfo;
import com.cyzest.findbooks.searcher.BookSearchParam;
import com.cyzest.findbooks.searcher.BookSearchResult;
import com.cyzest.findbooks.searcher.OpenApiBookSearchHelper;
import groovy.util.logging.Slf4j;
import lombok.AllArgsConstructor;
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
