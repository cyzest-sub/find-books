package com.cyzest.findbooks.searcher;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookSearchParam {

    private String categoryCode;
    private String targetCode;
    private String query;
    private BookSearchSort sort;
    private Integer page;
    private Integer size;

}
