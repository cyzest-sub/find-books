package com.cyzest.findbooks.dao;

import com.cyzest.findbooks.searcher.BookSearchSort;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class BookSearchHistory {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String query;

    @Column(nullable = false)
    private OpenApiType openApiType;

    @Column
    private String categoryCode;

    @Column
    private String targetCode;

    @Column
    private BookSearchSort sort;

    @Column(nullable = false)
    private LocalDateTime regDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

}
