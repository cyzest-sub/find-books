package com.cyzest.findbooks.searcher.kakao;

import com.cyzest.findbooks.common.BasedModelMapper;
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

    private final List<BookSearchCategory> bookSearchCategories;

    private final List<BookSearchTarget> bookSearchTargets;

    private final Map<String, BookSearchCategory> bookSearchCategoryMap;

    private final Map<String, BookSearchTarget> bookSearchTargetMap;

    private final RestTemplate restTemplate;

    private final ModelMapper modelMapper;

    private final String restApiKey;

    private final String API_PROTOCOL = "https";
    private final String API_HOST = "dapi.kakao.com";
    private final String API_AUTH_HEADER_NAME = HttpHeaders.AUTHORIZATION;
    private final String API_AUTH_HEADER_KEY_PREFIX = "KakaoAK";

    private final String SEARCH_BOOKS_URI = "/v2/search/book";

    private final int MAX_PAGE = 50;

    public KakaoOpenApiBookSearcher(RestTemplate restTemplate, String restApiKey) {

        if (restTemplate == null) {
            throw new IllegalArgumentException("restTemplate must not be null");
        }

        if (StringUtils.isEmpty(restApiKey)) {
            throw new IllegalArgumentException("restApiKey must not be null or empty");
        }

        this.restTemplate = restTemplate;
        this.restApiKey = restApiKey;

        modelMapper = new BasedModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        bookSearchCategories = getDefaultBookSearchCategories();
        bookSearchTargets = getDefaultBookSearchTargets();

        bookSearchCategoryMap = bookSearchCategories.stream()
                .collect(Collectors.toMap(BookSearchCategory::getCode, category -> category));

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

            String categoryCode = bookSearchParam.getCategoryCode();

            if (!StringUtils.isEmpty(categoryCode)) {
                if (!isAvailableBookSearchCategoryCode(categoryCode)) {
                    throw new IllegalArgumentException("categoryCode not available");
                }
                urlBuilder.queryParam("category", categoryCode);
            }

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
        return new ArrayList<>(bookSearchCategories);
    }

    @Override
    public BookSearchCategory getBookSearchCategory(String bookSearchCategoryCode) {
        return bookSearchCategoryMap.get(bookSearchCategoryCode);
    }

    @Override
    public boolean isAvailableBookSearchCategoryCode(String bookSearchCategoryCode) {
        return bookSearchCategoryMap.containsKey(bookSearchCategoryCode);
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

    private List<BookSearchCategory> getDefaultBookSearchCategories() {
        return Arrays.asList(
                new BookSearchCategory("1", "소설"),
                new BookSearchCategory("3", "시/에세이"),
                new BookSearchCategory("5", "인문"),
                new BookSearchCategory("7", "가정/생활"),
                new BookSearchCategory("8", "요리"),
                new BookSearchCategory("9", "건강"),
                new BookSearchCategory("11", "취미/스포츠"),
                new BookSearchCategory("13", "경제/경영"),
                new BookSearchCategory("15", "자기계발"),
                new BookSearchCategory("17", "정치/사회"),
                new BookSearchCategory("18", "정부간행물"),
                new BookSearchCategory("19", "역사/문화"),
                new BookSearchCategory("21", "종교"),
                new BookSearchCategory("23", "예술/대중문화"),
                new BookSearchCategory("25", "중/고등학습"),
                new BookSearchCategory("26", "기술/공학"),
                new BookSearchCategory("27", "외국어"),
                new BookSearchCategory("29", "과학"),
                new BookSearchCategory("31", "취업/수험서"),
                new BookSearchCategory("32", "여행/기행"),
                new BookSearchCategory("33", "컴퓨터/IT"),
                new BookSearchCategory("35", "잡지"),
                new BookSearchCategory("37", "사전"),
                new BookSearchCategory("38", "청소년"),
                new BookSearchCategory("39", "초등참고"),
                new BookSearchCategory("41", "유아"),
                new BookSearchCategory("42", "아동"),
                new BookSearchCategory("45", "어린이영어"),
                new BookSearchCategory("47", "만화"),
                new BookSearchCategory("50", "대학교재"),
                new BookSearchCategory("51", "어린이전집"),
                new BookSearchCategory("53", "한국소개도서")
        );
    }

    private List<BookSearchTarget> getDefaultBookSearchTargets() {
        return Arrays.asList(
                new BookSearchTarget("title", "제목"),
                new BookSearchTarget("isbn", "ISBN"),
                new BookSearchTarget("keyword", "주제어"),
                new BookSearchTarget("contents", "목차"),
                new BookSearchTarget("overview", "책소개"),
                new BookSearchTarget("publisher", "출판사"),
                new BookSearchTarget("author", "저자명")
        );
    }

}
