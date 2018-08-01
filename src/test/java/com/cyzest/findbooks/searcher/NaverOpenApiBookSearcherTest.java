package com.cyzest.findbooks.searcher;

import com.cyzest.findbooks.ExceptedAssert;
import com.cyzest.findbooks.FindBooksProperties;
import com.cyzest.findbooks.searcher.naver.NaverOpenApiBookSearcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class NaverOpenApiBookSearcherTest {

    private NaverOpenApiBookSearcher naverOpenApiBookSearcher;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FindBooksProperties findBooksProperties;

    @PostConstruct
    public void init() {
        naverOpenApiBookSearcher = new NaverOpenApiBookSearcher(
                restTemplate,
                findBooksProperties.getSearcher().getNaverClientId(),
                findBooksProperties.getSearcher().getNaverClientSecret()
        );
    }

    @Test
    public void 검색파라미터가_없을경우_결과를_만족하는가() {
        BookSearchResult bookSearchResult = naverOpenApiBookSearcher.search(null);
        Assert.assertNotNull(bookSearchResult);
        Assert.assertEquals(bookSearchResult.getTotalCount(), 0);
        Assert.assertNull(bookSearchResult.getBookInfos());
    }

    @Test
    public void 빈_검색파라미터의_경우_결과를_만족하는가() {
        ExceptedAssert.assertThrows(IllegalArgumentException.class,
                () -> naverOpenApiBookSearcher.search(new BookSearchParam()));
    }

    @Test
    public void 키워드_검색_결과를_만족하는가() {
        BookSearchParam bookSearchParam = new BookSearchParam();
        bookSearchParam.setQuery("자바");
        BookSearchResult bookSearchResult = naverOpenApiBookSearcher.search(bookSearchParam);
        Assert.assertTrue(bookSearchResult.getTotalCount() > 0);
        Assert.assertNotNull(bookSearchResult.getBookInfos());
        bookSearchResult.getBookInfos().forEach(bookInfo -> log.info("BookInfo : {}", bookInfo.getIsbn()));
    }

    @Test
    public void ISBN_타겟_검색_결과를_만족하는가() {
        BookSearchParam bookSearchParam = new BookSearchParam();
        bookSearchParam.setQuery("9791163030034");
        bookSearchParam.setTargetCode("d_isbn");
        BookSearchResult bookSearchResult = naverOpenApiBookSearcher.search(bookSearchParam);
        Assert.assertTrue(bookSearchResult.getTotalCount() > 0);
        Assert.assertNotNull(bookSearchResult.getBookInfos());
        Assert.assertNotNull(bookSearchResult.getBookInfos().get(0));
        Assert.assertEquals(bookSearchResult.getBookInfos().get(0).getIsbn(), "9791163030034");
        bookSearchResult.getBookInfos().forEach(bookInfo -> log.info("BookInfo : {}", bookInfo.getIsbn()));
    }

    @Test
    public void ISBN_개별_검색_결과를_만족하는가() {
        BookInfo bookInfo = naverOpenApiBookSearcher.searchByIsbn("9791163030034");
        Assert.assertNotNull(bookInfo);
        Assert.assertEquals(bookInfo.getIsbn(), "9791163030034");
        log.info("BookInfo : {}", bookInfo.getIsbn());
    }

    @Test
    public void 검색_최대_페이지_보다_큰_페이지_요청시_결과를_만족하는가() {
        int maxPage = naverOpenApiBookSearcher.getAvailableMaxPageByPageSize(9);
        Assert.assertTrue(0 < maxPage);
        BookSearchParam bookSearchParam = new BookSearchParam();
        bookSearchParam.setQuery("자바");
        bookSearchParam.setPage(maxPage + 1);
        ExceptedAssert.assertThrows(IllegalArgumentException.class,
                () -> naverOpenApiBookSearcher.search(bookSearchParam));
    }

}
