package com.cyzest.findbooks.service;

import com.cyzest.findbooks.searcher.BookSearchParam;
import com.cyzest.findbooks.searcher.BookSearchResult;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BookSearchServiceTest {

    @Autowired
    private BookSearchService bookSearchService;

    @Test
    public void 오픈API타입_파라미터가_없을경우_정상작동하는가() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(null, null));
    }

    @Test
    public void 검색_파라미터가_없을경우_결과를_만족하는가() {

        BookSearchResult kakaoBookSearchResult = bookSearchService.searchBooks(OpenApiType.KAKAO, null);
        Assertions.assertNotNull(kakaoBookSearchResult);
        Assertions.assertEquals(0, kakaoBookSearchResult.getTotalCount());
        Assertions.assertNull(kakaoBookSearchResult.getBookInfos());

        BookSearchResult naverBookSearchResult = bookSearchService.searchBooks(OpenApiType.NAVER, null);
        Assertions.assertNotNull(naverBookSearchResult);
        Assertions.assertEquals(0, naverBookSearchResult.getTotalCount());
        Assertions.assertNull(naverBookSearchResult.getBookInfos());
    }

    @Test
    public void 빈_검색_파라미터일경우_결과를_만족하는가() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(OpenApiType.KAKAO, new BookSearchParam()));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(OpenApiType.NAVER, new BookSearchParam()));
    }

    @Test
    public void 유효하지_않은_타겟코드_파라미터일_경우_정상작동하는가() {

        BookSearchParam bookSearchParam = new BookSearchParam();

        bookSearchParam.setTargetCode("test");

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(OpenApiType.KAKAO, bookSearchParam));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(OpenApiType.NAVER, bookSearchParam));
    }

}
