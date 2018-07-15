package com.cyzest.findbooks.searcher;

import lombok.Data;
import org.modelmapper.TypeToken;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Data
public class BookInfo {

    public static Type TYPE = new TypeToken<List<BookInfo>>() {}.getType();

    private String title;
    private String contents;
    private String url;
    private String isbn;
    private Date datetime;
    private List<String> authors;
    private String publisher;
    private List<String> translators;
    private Integer price;
    private Integer salePrice;
    private String category;
    private String thumbnail;

    public String getIsbn() {
        String isbn13 = null;
        if (!StringUtils.isEmpty(isbn)) {
            String[] isbnArray = isbn.split(" ");
            if (isbnArray.length > 0) {
                isbn13 = isbnArray[isbnArray.length - 1];
            }
        }
        return isbn13 != null ? isbn13 : "";
    }

}
