package com.cyzest.findbooks.model;

import com.cyzest.findbooks.dao.BookMark;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookMarkInfo {

    private Long id;
    private OpenApiType openApiType;
    private String isbn;
    private String title;
    private String thumbnail;
    private LocalDateTime regDate;

    public BookMarkInfo(BookMark bookMark) {
        if (bookMark != null) {
            this.id = bookMark.getId();
            this.openApiType = bookMark.getOpenApiType();
            this.isbn = bookMark.getIsbn();
            this.title = bookMark.getTitle();
            this.thumbnail = bookMark.getThumbnail();
            this.regDate = bookMark.getRegDate();
        }
    }

}
