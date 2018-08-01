package com.cyzest.findbooks.searcher;

import java.util.List;

public interface OpenApiBookSearcher {

    BookSearchResult search(BookSearchParam bookSearchParam);

    BookInfo searchByIsbn(String isbn);

    List<BookSearchCategory> getBookSearchCategories();

    BookSearchCategory getBookSearchCategory(String bookSearchCategoryCode);

    boolean isAvailableBookSearchCategoryCode(String bookSearchCategoryCode);

    List<BookSearchTarget> getBookSearchTargets();

    BookSearchTarget getBookSearchTarget(String bookSearchTargetCode);

    boolean isAvailableBookSearchTargetCode(String bookSearchTargetCode);

    int getAvailableMaxPageByPageSize(int pageSize);

}
