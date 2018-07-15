package com.cyzest.findbooks.controller;

import com.cyzest.findbooks.common.Paging;
import com.cyzest.findbooks.model.BookSearchHistoryResult;
import com.cyzest.findbooks.service.BookSearchHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class BookSearchHistoryController {

    private BookSearchHistoryService bookSearchHistoryService;

    @GetMapping("/history")
    public String getSearchHistories(
            Authentication authentication, Model model,
            @PageableDefault(size = 9, page = 1, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        BookSearchHistoryResult bookSearchHistoryResult = bookSearchHistoryService.getHistoriesByUserId(authentication.getName(), pageable);

        model.addAttribute("paging", new Paging(pageable.getPageNumber(), pageable.getPageSize(), bookSearchHistoryResult.getTotalCount()));
        model.addAttribute("searchHistoryInfos", bookSearchHistoryResult.getSearchHistoryInfos());

        return "history";
    }

}
