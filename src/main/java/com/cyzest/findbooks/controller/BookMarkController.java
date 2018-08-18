package com.cyzest.findbooks.controller;

import com.cyzest.findbooks.common.Paging;
import com.cyzest.findbooks.model.BookMarkPagingParam;
import com.cyzest.findbooks.model.BookMarkResult;
import com.cyzest.findbooks.model.BookMarkSort;
import com.cyzest.findbooks.searcher.OpenApiType;
import com.cyzest.findbooks.service.BookMarkService;
import io.github.cyzest.commons.spring.web.EnumCodePropertyEditor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditor;

@Slf4j
@Controller
@AllArgsConstructor
public class BookMarkController {

    private final PropertyEditor openApiTypePropertyEditor = new EnumCodePropertyEditor<>(OpenApiType.class);
    private final PropertyEditor bookMarkSortPropertyEditor = new EnumCodePropertyEditor<>(BookMarkSort.class);

    private BookMarkService bookMarkService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OpenApiType.class, openApiTypePropertyEditor);
        binder.registerCustomEditor(BookMarkSort.class, bookMarkSortPropertyEditor);
    }

    @PostMapping("/bookmark")
    @ResponseBody public String saveBookMark(
            Authentication authentication,
            @RequestParam OpenApiType openApiType, @RequestParam String isbn) throws Exception {

        bookMarkService.saveBookMark(authentication.getName(), openApiType, isbn);

        return "success";
    }

    @DeleteMapping("/bookmark/{id}")
    @ResponseBody public String deleteBookMark(@PathVariable long id, Authentication authentication) {

        bookMarkService.deleteBookMark(authentication.getName(), id);

        return "success";
    }

    @GetMapping("/bookmark")
    public String getBookMarks(
            @ModelAttribute BookMarkPagingParam pagingParam, Authentication authentication, Model model) {

        model.addAttribute("bookMarkSorts", BookMarkSort.getBookMarkSorts());

        BookMarkResult bookMarkResult = bookMarkService.getBookMarksByUserId(authentication.getName(), pagingParam);

        Paging paging = Paging.builder()
                .page(pagingParam.getPage())
                .size(pagingParam.getSize())
                .totalCount(bookMarkResult.getTotalCount())
                .build();

        model.addAttribute("paging", paging);
        model.addAttribute("bookMarkInfos", bookMarkResult.getBookMarkInfos());
        model.addAttribute("pagingParam", pagingParam);

        return "bookmark";
    }

}
