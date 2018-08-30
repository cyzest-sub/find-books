package com.cyzest.findbooks.searcher;

import com.cyzest.findbooks.FindBooksProperties;
import com.cyzest.findbooks.searcher.kakao.KakaoOpenApiBookSearcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class KakaoOpenApiBookSearcherTest {

    private KakaoOpenApiBookSearcher kakaoOpenApiBookSearcher;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FindBooksProperties findBooksProperties;

    private static final String TEST_TARGET_ISBN = "9788950974329";

    @PostConstruct
    public void init() {
        kakaoOpenApiBookSearcher = new KakaoOpenApiBookSearcher(
                restTemplate, findBooksProperties.getSearcher().getKakaoApiKey());
    }

    @Test
    public void 검색파라미터가_없을경우_결과를_만족하는가() {
        BookSearchResult bookSearchResult = kakaoOpenApiBookSearcher.search(null);
        Assertions.assertNotNull(bookSearchResult);
        Assertions.assertEquals(0, bookSearchResult.getTotalCount());
        Assertions.assertNull(bookSearchResult.getBookInfos());
    }

    @Test
    public void 빈_검색파라미터의_경우_결과를_만족하는가() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> kakaoOpenApiBookSearcher.search(new BookSearchParam()));
    }

    @Test
    public void 키워드_검색_결과를_만족하는가() {
        BookSearchParam bookSearchParam = new BookSearchParam();
        bookSearchParam.setQuery("카카오");
        BookSearchResult bookSearchResult = kakaoOpenApiBookSearcher.search(bookSearchParam);
        Assertions.assertTrue(bookSearchResult.getTotalCount() > 0);
        Assertions.assertNotNull(bookSearchResult.getBookInfos());
        bookSearchResult.getBookInfos().forEach(bookInfo -> log.info("BookInfo : {}", bookInfo.getIsbn()));
    }

    @Test
    public void ISBN_타겟_검색_결과를_만족하는가() {
        BookSearchParam bookSearchParam = new BookSearchParam();
        bookSearchParam.setQuery(TEST_TARGET_ISBN);
        bookSearchParam.setTargetCode("isbn");
        BookSearchResult bookSearchResult = kakaoOpenApiBookSearcher.search(bookSearchParam);
        Assertions.assertTrue(bookSearchResult.getTotalCount() > 0);
        Assertions.assertNotNull(bookSearchResult.getBookInfos());
        Assertions.assertNotNull(bookSearchResult.getBookInfos().get(0));
        Assertions.assertEquals(TEST_TARGET_ISBN, bookSearchResult.getBookInfos().get(0).getIsbn());
        bookSearchResult.getBookInfos().forEach(bookInfo -> log.info("BookInfo : {}", bookInfo.getIsbn()));
    }

    @Test
    public void ISBN_개별_검색_결과를_만족하는가() {
        BookInfo bookInfo = kakaoOpenApiBookSearcher.searchByIsbn(TEST_TARGET_ISBN);
        Assertions.assertNotNull(bookInfo);
        Assertions.assertEquals(TEST_TARGET_ISBN, bookInfo.getIsbn());
        log.info("BookInfo : {}", bookInfo.getIsbn());
    }

    @Test
    public void 검색_최대_페이지_보다_큰_페이지_요청시_결과를_만족하는가() {
        int maxPage = kakaoOpenApiBookSearcher.getAvailableMaxPageByPageSize(9);
        Assertions.assertTrue(0 < maxPage);
        BookSearchParam bookSearchParam = new BookSearchParam();
        bookSearchParam.setQuery("카카오");
        bookSearchParam.setPage(maxPage + 1);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> kakaoOpenApiBookSearcher.search(bookSearchParam));
    }

}
