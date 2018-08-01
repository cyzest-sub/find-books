package com.cyzest.findbooks.searcher.naver;

import com.cyzest.findbooks.searcher.BookInfo;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Data
public class NaverBookInfo {

    private String title;
    private String link;
    private String image;
    private String author;
    private Integer price;
    private Integer discount;
    private String publisher;
    private Date pubdate;
    private String isbn;
    private String description;

    public BookInfo createBookInfo() {

        BookInfo bookInfo = new BookInfo();

        bookInfo.setTitle(Optional.ofNullable(title).map(s -> s.replaceAll("<b>|</b>", "")).orElse(title));
        bookInfo.setUrl(link);
        bookInfo.setIsbn(isbn);
        bookInfo.setDatetime(pubdate);
        bookInfo.setContents(description);
        bookInfo.setThumbnail(image);
        bookInfo.setPrice(price);
        bookInfo.setSalePrice(discount);
        bookInfo.setPublisher(publisher);

        if (!StringUtils.isEmpty(author)) {
            bookInfo.setAuthors(Collections.singletonList(author));
        }

        return bookInfo;
    }

}
