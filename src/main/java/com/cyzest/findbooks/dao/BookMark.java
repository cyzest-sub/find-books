package com.cyzest.findbooks.dao;

import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class BookMark {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private OpenApiType openApiType;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    private String thumbnail;

    @Column(nullable = false)
    private LocalDateTime regDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

}
