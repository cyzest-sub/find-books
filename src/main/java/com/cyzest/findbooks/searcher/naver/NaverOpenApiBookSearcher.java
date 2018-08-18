package com.cyzest.findbooks.searcher.naver;

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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class NaverOpenApiBookSearcher implements OpenApiBookSearcher {

    private final List<BookSearchCategory> bookSearchCategories;

    private final List<BookSearchTarget> bookSearchTargets;

    private final Map<String, BookSearchCategory> bookSearchCategoryMap;

    private final Map<String, BookSearchTarget> bookSearchTargetMap;

    private final Map<BookSearchSort, String> naverSortCodeMap;

    private final RestTemplate restTemplate;

    private final ModelMapper modelMapper;

    private final String clientId;

    private final String clientSecret;

    private final String API_PROTOCOL = "https";
    private final String API_HOST = "openapi.naver.com";
    private final String CLIENT_ID_HEADER_NAME = "X-Naver-Client-Id";
    private final String CLIENT_SECRET_HEADER_NAME = "X-Naver-Client-Secret";

    private final String SEARCH_BOOKS_URI = "/v1/search/book.xml";
    private final String SEARCH_BOOKS_ADVANCED_URI = "/v1/search/book_adv.xml";

    private final int MAX_START = 1000;

    public NaverOpenApiBookSearcher(RestTemplate restTemplate, String clientId, String clientSecret) {

        if (restTemplate == null) {
            throw new IllegalArgumentException("restTemplate must not be null");
        }

        if (StringUtils.isEmpty(clientId)) {
            throw new IllegalArgumentException("restApiKey must not be null or empty");
        }

        if (StringUtils.isEmpty(clientSecret)) {
            throw new IllegalArgumentException("clientSecret must not be null or empty");
        }

        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

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

        naverSortCodeMap = new ConcurrentHashMap<>();

        naverSortCodeMap.put(BookSearchSort.ACCURACY, "sim");
        naverSortCodeMap.put(BookSearchSort.RECENCY, "date");
        naverSortCodeMap.put(BookSearchSort.SALES, "count");
    }

    @Override
    public BookSearchResult search(BookSearchParam bookSearchParam) {

        BookSearchResult bookSearchResult = new BookSearchResult();

        if (bookSearchParam != null) {

            String query = bookSearchParam.getQuery();

            if (StringUtils.isEmpty(query)) {
                throw new IllegalArgumentException("query must not be null or empty");
            }

            UriComponentsBuilder urlBuilder;

            String categoryCode = bookSearchParam.getCategoryCode();
            String targetCode = bookSearchParam.getTargetCode();

            if (!StringUtils.isEmpty(categoryCode) || !StringUtils.isEmpty(targetCode)) {

                urlBuilder = UriComponentsBuilder.fromHttpUrl(getNaverUrl(SEARCH_BOOKS_ADVANCED_URI));

                if (!StringUtils.isEmpty(targetCode)) {

                    if (!isAvailableBookSearchTargetCode(targetCode)) {
                        throw new IllegalArgumentException("targetCode not available");
                    }

                    urlBuilder.queryParam(targetCode, query);

                } else {
                    urlBuilder.queryParam("d_titl", query);
                }

                if (!StringUtils.isEmpty(categoryCode)) {
                    if (!isAvailableBookSearchCategoryCode(categoryCode)) {
                        throw new IllegalArgumentException("categoryCode not available");
                    }
                    urlBuilder.queryParam("d_catg", categoryCode);
                }

            } else {
                urlBuilder = UriComponentsBuilder.fromHttpUrl(getNaverUrl(SEARCH_BOOKS_URI));
                urlBuilder.queryParam("query", query);
            }

            Optional.ofNullable(bookSearchParam.getSort())
                    .ifPresent(sort -> urlBuilder.queryParam("sort", naverSortCodeMap.get(sort)));

            Integer page = Optional.ofNullable(bookSearchParam.getPage()).orElse(1);
            Integer size = Optional.ofNullable(bookSearchParam.getSize()).orElse(10);

            if (page > getAvailableMaxPageByPageSize(size)) {
                throw new IllegalArgumentException("page not available");
            }

            urlBuilder.queryParam("start", size * (page - 1) + 1);
            urlBuilder.queryParam("display", size);

            URI requestUri = urlBuilder.build().encode().toUri();

            log.debug("Request URI : {}", requestUri);

            ResponseEntity<NaverBookSearchResult> responseEntity = restTemplate.exchange(
                    requestUri, HttpMethod.GET, new HttpEntity<>(getNaverHttpHeaders()), NaverBookSearchResult.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {

                NaverBookChannel naverBookChannel = responseEntity.getBody().getChannel();

                bookSearchResult.setTotalCount(naverBookChannel.getTotal());

                List<NaverBookInfo> naverBookInfos = naverBookChannel.getItems();

                if (!CollectionUtils.isEmpty(naverBookInfos)) {
                    bookSearchResult.setBookInfos(
                            naverBookInfos.stream().map(NaverBookInfo::createBookInfo).collect(Collectors.toList()));
                }
            }
        }

        return bookSearchResult;
    }

    @Override
    public BookInfo searchByIsbn(String isbn) {

        BookInfo bookInfo = null;

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(getNaverUrl(SEARCH_BOOKS_ADVANCED_URI));

        urlBuilder.queryParam("d_isbn", isbn);

        URI requestUri = urlBuilder.build().encode().toUri();

        log.debug("Request URI : {}", requestUri);

        ResponseEntity<NaverBookSearchResult> responseEntity = restTemplate.exchange(
                requestUri, HttpMethod.GET, new HttpEntity<>(getNaverHttpHeaders()), NaverBookSearchResult.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            List<NaverBookInfo> naverBookInfos = responseEntity.getBody().getChannel().getItems();

            if (!CollectionUtils.isEmpty(naverBookInfos)) {
                bookInfo = naverBookInfos.get(0).createBookInfo();
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
        return ((MAX_START - 1) / pageSize) + 1;
    }

    private HttpHeaders getNaverHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put(CLIENT_ID_HEADER_NAME, Collections.singletonList(clientId));
        headers.put(CLIENT_SECRET_HEADER_NAME, Collections.singletonList(clientSecret));
        return headers;
    }

    private String getNaverUrl(String uri) {
        return API_PROTOCOL + "://" + API_HOST + uri;
    }

    private List<BookSearchCategory> getDefaultBookSearchCategories() {
        return Arrays.asList(
                new BookSearchCategory("100", "소설"),
                new BookSearchCategory("110", "시/에세이"),
                new BookSearchCategory("120", "인문"),
                new BookSearchCategory("130", "가정/생활/요리"),
                new BookSearchCategory("140", "건강"),
                new BookSearchCategory("150", "취미/레저"),
                new BookSearchCategory("160", "경제/경영"),
                new BookSearchCategory("170", "자기계발"),
                new BookSearchCategory("180", "사회"),
                new BookSearchCategory("190", "역사/문화"),
                new BookSearchCategory("200", "종교"),
                new BookSearchCategory("210", "예술/대중문화"),
                new BookSearchCategory("220", "학습/참고서"),
                new BookSearchCategory("230", "국어/외국어"),
                new BookSearchCategory("240", "사전"),
                new BookSearchCategory("250", "과학/공학"),
                new BookSearchCategory("260", "취업/수험서"),
                new BookSearchCategory("270", "여행/지도"),
                new BookSearchCategory("280", "컴퓨터/IT"),
                new BookSearchCategory("290", "잡지"),
                new BookSearchCategory("300", "청소년"),
                new BookSearchCategory("310", "유아"),
                new BookSearchCategory("320", "어린"),
                new BookSearchCategory("330", "만화"),
                new BookSearchCategory("340", "해외도서")
        );
    }

    private List<BookSearchTarget> getDefaultBookSearchTargets() {
        return Arrays.asList(
                new BookSearchTarget("d_titl", "제목"),
                new BookSearchTarget("d_isbn", "ISBN"),
                new BookSearchTarget("d_cont", "목차"),
                new BookSearchTarget("d_publ", "출판사"),
                new BookSearchTarget("d_auth", "저자명")
        );
    }

}
