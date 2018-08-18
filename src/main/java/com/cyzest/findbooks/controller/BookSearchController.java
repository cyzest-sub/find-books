package com.cyzest.findbooks.controller;

import com.cyzest.findbooks.common.Paging;
import com.cyzest.findbooks.model.OpenApiBookSearchParam;
import com.cyzest.findbooks.searcher.*;
import com.cyzest.findbooks.service.BookSearchHistoryService;
import com.cyzest.findbooks.service.BookSearchService;
import io.github.cyzest.commons.spring.web.EnumCodePropertyEditor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.beans.PropertyEditor;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class BookSearchController {

    private final PropertyEditor openApiTypePropertyEditor = new EnumCodePropertyEditor<>(OpenApiType.class);
    private final PropertyEditor bookSearchSortPropertyEditor = new EnumCodePropertyEditor<>(BookSearchSort.class);

    private BookSearchService bookSearchService;

    private BookSearchHistoryService bookSearchHistoryService;

    private OpenApiBookSearchHelper openApiBookSearchHelper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OpenApiType.class, openApiTypePropertyEditor);
        binder.registerCustomEditor(BookSearchSort.class, bookSearchSortPropertyEditor);
    }

    @GetMapping("/search")
    public String search(
            @ModelAttribute OpenApiBookSearchParam bookSearchParam, Authentication authentication, Model model) {

        OpenApiType openApiType = bookSearchParam.getOpenApiType();

        List<BookSearchCategory> bookSearchCategories =
                openApiBookSearchHelper.getBookSeachCategoriesByOpenApiType(openApiType);

        List<BookSearchTarget> bookSearchTargets =
                openApiBookSearchHelper.getBookSearchTargetsByOpenApiType(openApiType);

        model.addAttribute("openApiTypes", OpenApiType.getOpenApiTypes());
        model.addAttribute("bookSearchCategories", bookSearchCategories);
        model.addAttribute("bookSearchTargets", bookSearchTargets);
        model.addAttribute("bookSearchSorts", BookSearchSort.getBookSearchSorts());

        BookSearchResult bookSearchResult = null;

        if (!StringUtils.isEmpty(bookSearchParam.getQuery())) {

            bookSearchResult = bookSearchService.searchBooks(openApiType, bookSearchParam.createBookSearchParam());

            bookSearchHistoryService.saveHistory(authentication.getName(), bookSearchParam);

            Paging paging = Paging.builder()
                    .page(bookSearchParam.getPage())
                    .size(bookSearchParam.getSize())
                    .totalCount(bookSearchResult.getTotalCount())
                    .build();

            int maxPage =
                    openApiBookSearchHelper.getBookSearchMaxPageByOpenApiType(openApiType, bookSearchParam.getSize());

            model.addAttribute("paging", paging);
            model.addAttribute("maxPage", maxPage);
        }

        model.addAttribute("bookSearchResult", bookSearchResult);
        model.addAttribute("bookSearchParam", bookSearchParam);

        return "search";
    }

    @GetMapping("/books/{openApiType}/{isbn}")
    public String getBook(@PathVariable OpenApiType openApiType, @PathVariable String isbn, Model model) {

        model.addAttribute("openApiType", openApiType);
        model.addAttribute("bookInfo", bookSearchService.getBookByIsbn(openApiType, isbn));

        return "book";
    }

}
