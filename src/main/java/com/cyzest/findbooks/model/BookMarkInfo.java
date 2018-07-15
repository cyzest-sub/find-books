package com.cyzest.findbooks.model;

import com.cyzest.findbooks.service.OpenApiType;
import lombok.Data;

import java.util.Date;

@Data
public class BookMarkInfo {

    private Long id;
    private OpenApiType openApiType;
    private String isbn;
    private String title;
    private String thumbnail;
    private Date regDate;

}
