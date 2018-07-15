package com.cyzest.findbooks.controller;

import com.cyzest.findbooks.common.EnumCodePropertyEditor;
import com.cyzest.findbooks.common.Paging;
import com.cyzest.findbooks.model.OpenApiBookSearchParam;
import com.cyzest.findbooks.searcher.BookSearchResult;
import com.cyzest.findbooks.searcher.BookSearchSort;
import com.cyzest.findbooks.searcher.OpenApiBookSearchHelper;
import com.cyzest.findbooks.service.BookSearchHistoryService;
import com.cyzest.findbooks.service.BookSearchService;
import com.cyzest.findbooks.searcher.OpenApiType;
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
    public String search(@ModelAttribute OpenApiBookSearchParam bookSearchParam, Authentication authentication, Model model) throws Exception {

        OpenApiType openApiType = bookSearchParam.getOpenApiType();

        model.addAttribute("openApiTypes", OpenApiType.getOpenApiTypes());
        model.addAttribute("bookSearchCategories", openApiBookSearchHelper.getBookSeachCategoriesByOpenApiType(openApiType));
        model.addAttribute("bookSearchTargets", openApiBookSearchHelper.getBookSearchTargetsByOpenApiType(openApiType));
        model.addAttribute("bookSearchSorts", BookSearchSort.getBookSearchSorts());

        BookSearchResult bookSearchResult = null;

        if (!StringUtils.isEmpty(bookSearchParam.getQuery())) {

            bookSearchResult = bookSearchService.searchBooks(openApiType, bookSearchParam.createBookSearchParam());

            bookSearchHistoryService.saveHistory(authentication.getName(), bookSearchParam);

            model.addAttribute("paging", new Paging(bookSearchParam.getPage(), bookSearchParam.getSize(), bookSearchResult.getTotalCount()));
        }

        model.addAttribute("bookSearchResult", bookSearchResult);
        model.addAttribute("bookSearchParam", bookSearchParam);

        return "search";
    }

    @GetMapping("/books/{openApiType}/{isbn}")
    public String getBook(@PathVariable OpenApiType openApiType, @PathVariable String isbn, Authentication authentication, Model model) {

        model.addAttribute("openApiType", openApiType);
        model.addAttribute("bookInfo", bookSearchService.getBookByIsbn(openApiType, isbn));

        return "book";
    }

}
