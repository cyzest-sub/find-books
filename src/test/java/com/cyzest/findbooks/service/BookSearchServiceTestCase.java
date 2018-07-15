package com.cyzest.findbooks.service;

import com.cyzest.findbooks.dao.BookSearchHistoryRepository;
import groovy.util.logging.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BookSearchServiceTestCase {

    @Autowired
    private BookSearchService bookSearchService;

    @Autowired
    private BookSearchHistoryRepository bookSearchHistoryRepository;

}
