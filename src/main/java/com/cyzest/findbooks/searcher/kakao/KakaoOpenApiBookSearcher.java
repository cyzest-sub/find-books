package com.cyzest.findbooks.searcher.kakao;

import com.cyzest.findbooks.searcher.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class KakaoOpenApiBookSearcher implements OpenApiBookSearcher {

    private final List<BookSearchTarget> bookSearchTargets;

    private final Map<String, BookSearchTarget> bookSearchTargetMap;

    private final RestTemplate restTemplate;

    private final ModelMapper modelMapper;

    private final String restApiKey;

    private static final String API_PROTOCOL = "https";
    private static final String API_HOST = "dapi.kakao.com";
    private static final String API_AUTH_HEADER_NAME = HttpHeaders.AUTHORIZATION;
    private static final String API_AUTH_HEADER_KEY_PREFIX = "KakaoAK";

    private static final String SEARCH_BOOKS_URI = "/v3/search/book";

    private static final int MAX_PAGE = 100;

    public KakaoOpenApiBookSearcher(RestTemplate restTemplate, String restApiKey) {

        if (restTemplate == null) {
            throw new IllegalArgumentException("restTemplate must not be null");
        }

        if (StringUtils.isEmpty(restApiKey)) {
            throw new IllegalArgumentException("restApiKey must not be null or empty");
        }

        this.restTemplate = restTemplate;
        this.restApiKey = restApiKey;

        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        bookSearchTargets = getDefaultBookSearchTargets();
        bookSearchTargetMap = bookSearchTargets.stream()
                .collect(Collectors.toMap(BookSearchTarget::getCode, target -> target));
    }

    @Override
    public BookSearchResult search(BookSearchParam bookSearchParam) {

        BookSearchResult bookSearchResult = new BookSearchResult();

        if (bookSearchParam != null) {

            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(getKaKaoUrl(SEARCH_BOOKS_URI));

            String query = bookSearchParam.getQuery();

            if (StringUtils.isEmpty(query)) {
                throw new IllegalArgumentException("query must not be null or empty");
            }

            urlBuilder.queryParam("query", query);

            String targetCode = bookSearchParam.getTargetCode();

            if (!StringUtils.isEmpty(targetCode)) {
                if (!isAvailableBookSearchTargetCode(targetCode)) {
                    throw new IllegalArgumentException("targetCode not available");
                }
                urlBuilder.queryParam("target", targetCode);
            }

            Optional.ofNullable(bookSearchParam.getSort())
                    .ifPresent(sort -> urlBuilder.queryParam("sort", sort.getCode()));

            Integer page = Optional.ofNullable(bookSearchParam.getPage()).orElse(1);
            Integer size = Optional.ofNullable(bookSearchParam.getSize()).orElse(10);

            if (page > getAvailableMaxPageByPageSize(size)) {
                throw new IllegalArgumentException("page not available");
            }

            urlBuilder.queryParam("page", page);
            urlBuilder.queryParam("size", size);

            URI requestUri = urlBuilder.build().encode().toUri();

            log.debug("Request URI : {}", requestUri);

            ResponseEntity<KakaoBookSearchResult> responseEntity = restTemplate.exchange(
                    requestUri, HttpMethod.GET, new HttpEntity<>(getKakaoHttpHeaders()), KakaoBookSearchResult.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {

                KakaoBookSearchResult kakaoBookSearchResult = responseEntity.getBody();

                bookSearchResult.setTotalCount(kakaoBookSearchResult.getMeta().get("total_count").asInt());

                List<KakaoBookInfo> kakaoBookInfos = kakaoBookSearchResult.getDocuments();

                if (!CollectionUtils.isEmpty(kakaoBookInfos)) {
                    bookSearchResult.setBookInfos(modelMapper.map(kakaoBookInfos, BookInfo.TYPE));
                }
            }
        }

        return bookSearchResult;
    }

    @Override
    public BookInfo searchByIsbn(String isbn) {

        BookInfo bookInfo = null;

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(getKaKaoUrl(SEARCH_BOOKS_URI));

        urlBuilder.queryParam("target", "isbn");
        urlBuilder.queryParam("query", isbn);

        ResponseEntity<KakaoBookSearchResult> responseEntity = restTemplate.exchange(
                urlBuilder.build().encode().toUri(), HttpMethod.GET,
                new HttpEntity<>(getKakaoHttpHeaders()), KakaoBookSearchResult.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            KakaoBookSearchResult kakaoBookSearchResult = responseEntity.getBody();

            List<KakaoBookInfo> kakaoBookInfos = kakaoBookSearchResult.getDocuments();

            if (!CollectionUtils.isEmpty(kakaoBookInfos)) {
                bookInfo = modelMapper.map(kakaoBookInfos.get(0), BookInfo.class);
            }
        }

        return bookInfo;
    }

    @Override
    public List<BookSearchCategory> getBookSearchCategories() {
        return Collections.emptyList();
    }

    @Override
    public BookSearchCategory getBookSearchCategory(String bookSearchCategoryCode) {
        return null;
    }

    @Override
    public boolean isAvailableBookSearchCategoryCode(String bookSearchCategoryCode) {
        return false;
    }

    @Override
    public List<BookSearchTarget> getBookSearchTargets() {
        return new ArrayList<>(bookSearchTargets);
    }

    @Override
    public BookSearchTarget getBookSearchTarget(String bookSearchTargetCode) {
        return bookSearchTargetMap.get(bookSearchTargetCode);
    }

    @Override
    public boolean isAvailableBookSearchTargetCode(String bookSearchTargetCode) {
        return bookSearchTargetMap.containsKey(bookSearchTargetCode);
    }

    @Override
    public int getAvailableMaxPageByPageSize(int pageSize) {
        return MAX_PAGE;
    }

    private HttpHeaders getKakaoHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put(API_AUTH_HEADER_NAME, Collections.singletonList(API_AUTH_HEADER_KEY_PREFIX + " " + restApiKey));
        return headers;
    }

    private String getKaKaoUrl(String uri) {
        return API_PROTOCOL + "://" + API_HOST + uri;
    }

    private List<BookSearchTarget> getDefaultBookSearchTargets() {
        return Arrays.asList(
                new BookSearchTarget("title", "제목"),
                new BookSearchTarget("isbn", "ISBN"),
                new BookSearchTarget("publisher", "출판사"),
                new BookSearchTarget("person", "인명")
        );
    }

}
