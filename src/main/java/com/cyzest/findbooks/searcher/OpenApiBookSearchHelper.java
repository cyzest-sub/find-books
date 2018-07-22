package com.cyzest.findbooks.searcher;

import com.cyzest.findbooks.FindBooksProperties;
import com.cyzest.findbooks.searcher.kakao.KakaoOpenApiBookSearcher;
import com.cyzest.findbooks.searcher.naver.NaverOpenApiBookSearcher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OpenApiBookSearchHelper {

    private final Map<OpenApiType, OpenApiBookSearcher> openApiBookSearcherMap = new ConcurrentHashMap<>();

    public OpenApiBookSearchHelper(RestTemplate restTemplate, FindBooksProperties findBooksProperties) {
        FindBooksProperties.Searcher searcher = findBooksProperties.getSearcher();
        openApiBookSearcherMap.put(
                OpenApiType.KAKAO,
                new KakaoOpenApiBookSearcher(restTemplate, searcher.getKakaoApiKey()));
        openApiBookSearcherMap.put(
                OpenApiType.NAVER,
                new NaverOpenApiBookSearcher(restTemplate, searcher.getNaverClientId(), searcher.getNaverClientSecret()));
    }

    public List<BookSearchCategory> getBookSeachCategoriesByOpenApiType(OpenApiType openApiType) {
        return getOpenApiBookSearcher(openApiType).getBookSearchCategories();
    }

    public List<BookSearchTarget> getBookSearchTargetsByOpenApiType(OpenApiType openApiType) {
        return getOpenApiBookSearcher(openApiType).getBookSearchTargets();
    }

    public BookSearchCategory getBookSearchCategoryByOpenApiType(OpenApiType openApiType, String categoryCode) {
        return getOpenApiBookSearcher(openApiType).getBookSearchCategory(categoryCode);
    }

    public BookSearchTarget getBookSearchTargetByOpenApiType(OpenApiType openApiType, String targetCode) {
        return getOpenApiBookSearcher(openApiType).getBookSearchTarget(targetCode);
    }

    public OpenApiBookSearcher getOpenApiBookSearcher(OpenApiType openApiType) {

        OpenApiBookSearcher openApiBookSearcher = openApiBookSearcherMap.get(openApiType);

        if (openApiBookSearcher == null) {
            throw new IllegalArgumentException(openApiType.getCode() + " openApiBookSearcher is null");
        }

        return openApiBookSearcher;
    }

}
