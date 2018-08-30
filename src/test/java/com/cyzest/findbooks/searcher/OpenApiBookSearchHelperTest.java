package com.cyzest.findbooks.searcher;

import com.cyzest.findbooks.FindBooksProperties;
import com.cyzest.findbooks.searcher.kakao.KakaoOpenApiBookSearcher;
import com.cyzest.findbooks.searcher.naver.NaverOpenApiBookSearcher;
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
public class OpenApiBookSearchHelperTest {

    private OpenApiBookSearchHelper openApiBookSearchHelper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FindBooksProperties findBooksProperties;

    @PostConstruct
    public void init() {
        openApiBookSearchHelper = new OpenApiBookSearchHelper(restTemplate, findBooksProperties);
    }

    @Test
    public void 오픈API타입에_맞는_구현체가_반환되는가() {

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> openApiBookSearchHelper.getOpenApiBookSearcher(null));

        Assertions.assertTrue(
                openApiBookSearchHelper.getOpenApiBookSearcher(OpenApiType.KAKAO) instanceof KakaoOpenApiBookSearcher);
        Assertions.assertTrue(
                openApiBookSearchHelper.getOpenApiBookSearcher(OpenApiType.NAVER) instanceof NaverOpenApiBookSearcher);
    }

    @Test
    public void 오픈API타입에_맞는_카테고리목록을_반환하는가() {

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> openApiBookSearchHelper.getBookSeachCategoriesByOpenApiType(null));

        openApiBookSearchHelper.getBookSeachCategoriesByOpenApiType(OpenApiType.KAKAO)
                .forEach(bookSearchCategory -> Assertions.assertTrue(
                        openApiBookSearchHelper.getOpenApiBookSearcher(OpenApiType.KAKAO)
                                .isAvailableBookSearchCategoryCode(bookSearchCategory.getCode())));
        openApiBookSearchHelper.getBookSeachCategoriesByOpenApiType(OpenApiType.NAVER)
                .forEach(bookSearchCategory -> Assertions.assertTrue(
                        openApiBookSearchHelper.getOpenApiBookSearcher(OpenApiType.NAVER)
                                .isAvailableBookSearchCategoryCode(bookSearchCategory.getCode())));
    }

    @Test
    public void 오픈API타입에_맞는_타겟목록을_반환하는가() {

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> openApiBookSearchHelper.getBookSearchTargetsByOpenApiType(null));

        openApiBookSearchHelper.getBookSearchTargetsByOpenApiType(OpenApiType.KAKAO)
                .forEach(bookSearchCategory -> Assertions.assertTrue(
                        openApiBookSearchHelper.getOpenApiBookSearcher(OpenApiType.KAKAO)
                                .isAvailableBookSearchTargetCode(bookSearchCategory.getCode())));
        openApiBookSearchHelper.getBookSearchTargetsByOpenApiType(OpenApiType.NAVER)
                .forEach(bookSearchCategory -> Assertions.assertTrue(
                        openApiBookSearchHelper.getOpenApiBookSearcher(OpenApiType.NAVER)
                                .isAvailableBookSearchTargetCode(bookSearchCategory.getCode())));
    }

}
