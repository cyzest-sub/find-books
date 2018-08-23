package com.cyzest.findbooks.service;

import com.cyzest.findbooks.dao.BookSearchHistory;
import com.cyzest.findbooks.dao.BookSearchHistoryRepository;
import com.cyzest.findbooks.dao.UserRepository;
import com.cyzest.findbooks.model.BookSearchHistoryInfo;
import com.cyzest.findbooks.model.BookSearchHistoryResult;
import com.cyzest.findbooks.model.OpenApiBookSearchParam;
import com.cyzest.findbooks.searcher.OpenApiBookSearchHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookSearchHistoryService {

    private UserRepository userRepository;

    private BookSearchHistoryRepository bookSearchHistoryRepository;

    private OpenApiBookSearchHelper openApiBookSearchHelper;

    public void saveHistory(String userId, OpenApiBookSearchParam openApiBookSearchParam) {

        userRepository.findById(userId).ifPresent(user -> {

            BookSearchHistory bookSearchHistory = new BookSearchHistory();

            bookSearchHistory.setOpenApiType(openApiBookSearchParam.getOpenApiType());
            bookSearchHistory.setCategoryCode(openApiBookSearchParam.getCategoryCode());
            bookSearchHistory.setTargetCode(openApiBookSearchParam.getTargetCode());
            bookSearchHistory.setSort(openApiBookSearchParam.getSort());
            bookSearchHistory.setQuery(openApiBookSearchParam.getQuery());
            bookSearchHistory.setRegDate(LocalDateTime.now());
            bookSearchHistory.setUser(user);

            bookSearchHistoryRepository.saveAndFlush(bookSearchHistory);

        });
    }

    public BookSearchHistoryResult getHistoriesByUserId(String userId, Pageable pageable) {

        BookSearchHistoryResult bookSearchHistoryResult = new BookSearchHistoryResult();

        userRepository.findById(userId).ifPresent(user -> {

            PageRequest pageRequest = PageRequest.of(
                    pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

            Page<BookSearchHistory> bookSearchHistoriesPage = bookSearchHistoryRepository.findByUser(user, pageRequest);

            List<BookSearchHistory> bookSearchHistories = bookSearchHistoriesPage.getContent();

            bookSearchHistoryResult.setTotalCount((int) bookSearchHistoriesPage.getTotalElements());

            if (bookSearchHistories != null) {
                bookSearchHistoryResult.setSearchHistoryInfos(
                        bookSearchHistories.stream().map(bookSearchHistory -> {
                            BookSearchHistoryInfo bookSearchHistoryInfo = new BookSearchHistoryInfo();
                            bookSearchHistoryInfo.setId(bookSearchHistory.getId());
                            bookSearchHistoryInfo.setOpenApiType(bookSearchHistory.getOpenApiType());
                            bookSearchHistoryInfo.setQuery(bookSearchHistory.getQuery());
                            bookSearchHistoryInfo.setSort(bookSearchHistory.getSort());
                            bookSearchHistoryInfo.setRegDate(bookSearchHistory.getRegDate());
                            bookSearchHistoryInfo.setCategory(
                                    openApiBookSearchHelper.getBookSearchCategoryByOpenApiType(
                                            bookSearchHistory.getOpenApiType(), bookSearchHistory.getCategoryCode()));
                            bookSearchHistoryInfo.setTarget(
                                    openApiBookSearchHelper.getBookSearchTargetByOpenApiType(
                                            bookSearchHistory.getOpenApiType(), bookSearchHistory.getTargetCode()));
                            return bookSearchHistoryInfo;
                        }).collect(Collectors.toList())
                );
            }

        });

        return bookSearchHistoryResult;
    }

}
