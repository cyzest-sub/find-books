package com.cyzest.findbooks.service;

import com.cyzest.findbooks.ExceptedAssert;
import com.cyzest.findbooks.searcher.BookSearchParam;
import com.cyzest.findbooks.searcher.BookSearchResult;
import com.cyzest.findbooks.searcher.OpenApiType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BookSearchServiceTest {

    @Autowired
    private BookSearchService bookSearchService;

    @Test
    public void 오픈API타입_파라미터가_없을경우_정상작동하는가() {
        ExceptedAssert.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(null, null));
    }

    @Test
    public void 검색_파라미터가_없을경우_결과를_만족하는가() {

        BookSearchResult kakaoBookSearchResult = bookSearchService.searchBooks(OpenApiType.KAKAO, null);
        Assert.assertNotNull(kakaoBookSearchResult);
        Assert.assertEquals(kakaoBookSearchResult.getTotalCount(), 0);
        Assert.assertNull(kakaoBookSearchResult.getBookInfos());

        BookSearchResult naverBookSearchResult = bookSearchService.searchBooks(OpenApiType.NAVER, null);
        Assert.assertNotNull(naverBookSearchResult);
        Assert.assertEquals(naverBookSearchResult.getTotalCount(), 0);
        Assert.assertNull(naverBookSearchResult.getBookInfos());
    }

    @Test
    public void 빈_검색_파라미터일경우_결과를_만족하는가() {

        ExceptedAssert.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(OpenApiType.KAKAO, new BookSearchParam()));

        ExceptedAssert.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(OpenApiType.NAVER, new BookSearchParam()));
    }

    @Test
    public void 유효하지_않은_타겟코드_파라미터일_경우_정상작동하는가() {

        BookSearchParam bookSearchParam = new BookSearchParam();

        bookSearchParam.setTargetCode("test");

        ExceptedAssert.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(OpenApiType.KAKAO, bookSearchParam));

        ExceptedAssert.assertThrows(IllegalArgumentException.class,
                () -> bookSearchService.searchBooks(OpenApiType.NAVER, bookSearchParam));
    }

}
