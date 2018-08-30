package com.cyzest.findbooks.service;

import com.cyzest.findbooks.dao.BookSearchHistory;
import com.cyzest.findbooks.dao.BookSearchHistoryRepository;
import com.cyzest.findbooks.dao.User;
import com.cyzest.findbooks.dao.UserRepository;
import com.cyzest.findbooks.model.BookSearchHistoryResult;
import com.cyzest.findbooks.model.OpenApiBookSearchParam;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BookSearchHistoryServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookSearchHistoryRepository bookSearchHistoryRepository;

    @Autowired
    private BookSearchHistoryService bookSearchHistoryService;

    private static final String TEST_USER_ID = "test";

    @PostConstruct
    public void init() {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setPassword(new BCryptPasswordEncoder().encode("test"));
        user.setRegDate(LocalDateTime.now());
        userRepository.saveAndFlush(user);
    }

    @Test
    @Rollback
    @Transactional
    public void 검색_히스토리를_정상적으로_저장하는가() {
        OpenApiBookSearchParam openApiBookSearchParam = new OpenApiBookSearchParam();
        openApiBookSearchParam.setQuery("test");
        bookSearchHistoryService.saveHistory(TEST_USER_ID, openApiBookSearchParam);
    }

    @Test
    public void 검색_히스토리_저장시_유효하지_않은_파라미터일_경우_정상작동하는가() {
        OpenApiBookSearchParam openApiBookSearchParam = new OpenApiBookSearchParam();
        openApiBookSearchParam.setOpenApiType(OpenApiType.KAKAO);
        openApiBookSearchParam.setCategoryCode("test");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookSearchHistoryService.saveHistory(TEST_USER_ID, openApiBookSearchParam));
    }

    @Test
    public void 검색_히스토리_저장시_쿼리_파라미터가_없을경우_정상작동하는가() {
        OpenApiBookSearchParam openApiBookSearchParam = new OpenApiBookSearchParam();
        openApiBookSearchParam.setOpenApiType(OpenApiType.KAKAO);
        openApiBookSearchParam.setQuery(null);
        Assertions.assertThrows(Throwable.class,
                () -> bookSearchHistoryService.saveHistory(TEST_USER_ID, openApiBookSearchParam));
    }

    @Test
    @Rollback
    @Transactional
    public void 검색_히스토리_결과가_정상적으로_반환되는가() {

        BookSearchHistory bookSearchHistory = new BookSearchHistory();
        bookSearchHistory.setOpenApiType(OpenApiType.KAKAO);
        bookSearchHistory.setQuery("test");
        bookSearchHistory.setRegDate(LocalDateTime.now());
        bookSearchHistory.setUser(userRepository.findById(TEST_USER_ID).get());
        bookSearchHistoryRepository.saveAndFlush(bookSearchHistory);

        BookSearchHistoryResult bookSearchHistoryResult =
                bookSearchHistoryService.getHistoriesByUserId(TEST_USER_ID, PageRequest.of(1, 10));

        Assertions.assertNotNull(bookSearchHistoryResult);
        Assertions.assertEquals(1, bookSearchHistoryResult.getTotalCount());
        Assertions.assertEquals(1, bookSearchHistoryResult.getSearchHistoryInfos().size());
        Assertions.assertEquals("test", bookSearchHistoryResult.getSearchHistoryInfos().get(0).getQuery());
    }

}
