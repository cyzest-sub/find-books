package com.cyzest.findbooks.model;

import com.cyzest.findbooks.searcher.BookSearchParam;
import com.cyzest.findbooks.searcher.BookSearchSort;
import com.cyzest.findbooks.service.OpenApiType;
import lombok.Data;

@Data
public class OpenApiBookSearchParam {

    private OpenApiType openApiType = OpenApiType.KAKAO;
    private String categoryCode;
    private String targetCode;
    private String query;
    private BookSearchSort sort = BookSearchSort.ACCURACY;
    private Integer page = 1;
    private Integer size = 9;

    public BookSearchParam createBookSearchParam() {
        BookSearchParam bookSearchParam = new BookSearchParam();
        bookSearchParam.setCategoryCode(categoryCode);
        bookSearchParam.setTargetCode(targetCode);
        bookSearchParam.setQuery(query);
        bookSearchParam.setSort(sort);
        bookSearchParam.setPage(page);
        bookSearchParam.setSize(size);
        return bookSearchParam;
    }

}
